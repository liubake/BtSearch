package com.erola.btsearch.spider.dht.server;

import com.erola.btsearch.spider.config.SpiderConfig;
import com.erola.btsearch.spider.dht.handler.PeerDownloadHandler;
import com.erola.btsearch.spider.dht.listener.OnTorrentDownloadListener;
import com.erola.btsearch.spider.dht.model.DownloadPeer;
import com.erola.btsearch.spider.dht.model.Node;
import com.erola.btsearch.spider.dht.util.ByteUtil;
import com.erola.btsearch.util.log4j.Log4jHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.ardverk.coding.BencodingInputStream;
import org.ardverk.coding.BencodingOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Erola on 2017/9/8.
 */
public class DHTServer implements Runnable {
    private int port;
    private byte[] nodeId;
    private String hostName;
    private int downloadPeerQueueSize;
    private int downloadPeerThreadCount;
    private int nodeConnectTimeOut;
    private int nodeReadWriteTimeOut;
    private BlockingQueue<Node> nodeQueue;
    private List<InetSocketAddress> rootNodeList;
    private PeerDownloadHandler peerDownloadHandler;
    private Thread peerDownloaderThread;
    private Channel channel;
    private final Bootstrap bootstrap;
    private OnTorrentDownloadListener onTorrentDownloadListener;

    public DHTServer(OnTorrentDownloadListener onTorrentDownloadListener) {
        this.onTorrentDownloadListener = onTorrentDownloadListener;
        port = SpiderConfig.getPort();
        hostName = SpiderConfig.getHostName();
        downloadPeerQueueSize = SpiderConfig.getDownloadPeerQueueSize();
        downloadPeerThreadCount = SpiderConfig.getDownloadPeerThreadCount();
        nodeConnectTimeOut = SpiderConfig.getNodeConnectTimeOut();
        nodeReadWriteTimeOut = SpiderConfig.getNodeReadWriteTimeOut();
        nodeQueue = new LinkedBlockingQueue<>(SpiderConfig.getMaxNodeQueueSize());
        rootNodeList = SpiderConfig.getDhtRootNodeList();
        nodeId = ByteUtil.createRandomNodeId();
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup()).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_SNDBUF, 65536).option(ChannelOption.SO_RCVBUF, 65536)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new PacketHandler());
                    }
                });
        peerDownloadHandler = new PeerDownloadHandler(nodeId, downloadPeerThreadCount, downloadPeerQueueSize, nodeConnectTimeOut, nodeReadWriteTimeOut, this.onTorrentDownloadListener);
        peerDownloaderThread = new Thread(this.peerDownloadHandler);
        peerDownloaderThread.setDaemon(true);
        peerDownloaderThread.start();
    }

    /**
     *
     */
    @Override
    public void run() {
        this.channel = this.bootstrap.bind(this.port).syncUninterruptibly().channel();
        while (true) {
            try {
                Node item = nodeQueue.poll();
                if (item == null) {
                    for (InetSocketAddress address : rootNodeList) {
                        //每次用随机生成的 NodeId 是为了获取更多不同的节点
                        joinDHT(address, nodeId, geRandomtNodeId());
                    }
                    Thread.sleep(1000);
                } else {
                    //每次用随机生成的 NodeId 是为了获取更多不同的节点
                    joinDHT(new InetSocketAddress(item.getIp(), item.getPort()), nodeId, geRandomtNodeId());
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     *
     * @return
     */
    private byte[] geRandomtNodeId(){
        return ByteUtil.createRandomNodeId();
    }

    /**
     *
     * @param address
     * @param nid
     * @param target
     */
    private void joinDHT(InetSocketAddress address, byte[] nid, byte[] target) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("target", target);
        if (nid != null) {
            map.put("id", nid);
        }
        sendKRPC(address, createQueries("find_node".getBytes(), "q", map));
    }

    /**
     *
     * @param info_hash
     * @return
     */
    private byte[] getNeighbor(byte[] info_hash) {
        byte[] bytes = new byte[20];
        System.arraycopy(info_hash, 0, bytes, 0, 10);
        System.arraycopy(this.nodeId, 10, bytes, 10, 10);
        return bytes;
    }

    /**
     *
     * @param t
     * @param y
     * @param arg
     * @return
     */
    private Map<String, ?> createQueries(byte[] t, String y, Map<String, Object> arg) {
        Map<String, Object> map = new HashMap<>();
        map.put("t", t);
        map.put("y", y);
        if (!arg.containsKey("id")) {
            arg.put("id", this.nodeId);
        }
        if (y.equals("q")) {
            map.put("q", new String(t));
            map.put("a", arg);
        } else {
            map.put("r", arg);
        }
        return map;
    }

    /**
     *
     * @param address
     * @param map
     */
    private void packetProcessing(InetSocketAddress address, Map<String, ?> map) {
        String y = new String((byte[]) map.get("y"));
        if (y.equals("q"))
            query(address, (byte[]) map.get("t"), new String((byte[]) map.get("q")), (Map<String, ?>) map.get("a"));
        else if (y.equals("r"))
            response(address, (byte[]) map.get("t"), (Map<String, ?>) map.get("r"));
    }

    /**
     *
     * @param address
     * @param t
     * @param q
     * @param a
     */
    private void query(InetSocketAddress address, byte[] t, String q, Map<String, ?> a) {
        if (q.equals("ping"))
            queryPing(address, t);
        else if (q.equals("find_node"))
            queryFindNode(address, t, (byte[]) a.get("target"));
        else if (q.equals("get_peers"))
            queryGetPeers(address, t, (byte[]) a.get("info_hash"));
        else if (q.equals("announce_peer")) {
            if (a.containsKey("implied_port") && ((BigInteger) a.get("implied_port")).intValue() != 0) {
                queryAnnouncePeer(address, t, (byte[]) a.get("info_hash"), address.getPort(), (byte[]) a.get("token"));
            } else {
                queryAnnouncePeer(address, t, (byte[]) a.get("info_hash"), ((BigInteger) a.get("port")).intValue(), (byte[]) a.get("token"));
            }
        }
    }

    /**
     *
     * @param address
     * @param t
     */
    private void queryPing(InetSocketAddress address, byte[] t) {
        sendKRPC(address, createQueries(t, "r", new HashMap<>()));
    }

    /**
     *
     * @param address
     * @param t
     */
    private void queryFindNode(InetSocketAddress address, byte[] t, byte[] target) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("nodes", new byte[]{});
        sendKRPC(address, createQueries(t, "r", map));
    }

    /**
     *
     * @param address
     * @param t
     * @param info_hash
     */
    private void queryGetPeers(InetSocketAddress address, byte[] t, byte[] info_hash) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", new byte[]{info_hash[0], info_hash[1]});
        map.put("nodes", new byte[]{});
        map.put("id", getNeighbor(info_hash));
        sendKRPC(address, createQueries(t, "r", map));
    }

    /**
     *
     * @param address
     * @param t
     * @param info_hash
     * @param port
     * @param token
     */
    private void queryAnnouncePeer(InetSocketAddress address, byte[] t, byte[] info_hash, int port, byte[] token) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", getNeighbor(info_hash));
        sendKRPC(address, createQueries(t, "r", map));
        if (Arrays.equals(token, Arrays.copyOfRange(info_hash, 0, 2))) {
            peerDownloadHandler.addDownloadPeer(new DownloadPeer(address.getHostString(), port, info_hash));
            Log4jHelper.logDebug(this.getClass(), "AnnouncePeer:"+ByteUtil.byteArrayToHex(info_hash));
        }
    }

    /**
     *
     * @param address
     * @param t
     * @param r
     */
    private void response(InetSocketAddress address, byte[] t, Map<String, ?> r) {
        if (t == null)
            return;
        String str = new String(t);
        if (str.equals("ping"))
            responsePing(address);
        else if (str.equals("find_node"))
            responseFindNode(address, (byte[]) r.get("nodes"));
        else
            responseGetPeers(address, t, r);
    }

    /**
     *
     * @param address
     */
    private void responsePing(InetSocketAddress address) {
        //TODO
    }

    /**
     *
     * @param address
     * @param info_hash
     * @param r
     */
    private void responseGetPeers(InetSocketAddress address, byte[] info_hash, Map<String, ?> r) {
        //TODO
    }

    /**
     *
     * @param address
     * @param nodes
     */
    private void responseFindNode(InetSocketAddress address, byte[] nodes) {
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i += 26) {
                try {
                    InetAddress inetAddress = InetAddress.getByAddress(new byte[]{nodes[i + 20], nodes[i + 21], nodes[i + 22], nodes[i + 23]});
                    InetSocketAddress nodeAddress = new InetSocketAddress(inetAddress, (0x0000FF00 & (nodes[i + 24] << 8)) | (0x000000FF & nodes[i + 25]));
                    if (!nodeAddress.getHostString().equals(this.hostName)) {
                        byte[] nodeId = new byte[20];
                        System.arraycopy(nodes, i, nodeId, 0, 20);
                        nodeQueue.offer(new Node(nodeAddress.getHostString(), nodeAddress.getPort(), nodeId));
                    }
                    Log4jHelper.logDebug(this.getClass(), "FindNode:"+ ByteUtil.byteArrayToHex(nodeId));
                }
                catch (UnknownHostException e) {
                }
                catch (IllegalArgumentException ex) {
                }
            }
        }
    }

    /**
     * @param address
     * @param map
     */
    private void sendKRPC(InetSocketAddress address, Map<String, ?> map) {
        ByteArrayOutputStream contentStream = null;
        BencodingOutputStream bencodeStream = null;
        try {
            contentStream = new ByteArrayOutputStream();
            bencodeStream = new BencodingOutputStream(contentStream);
            bencodeStream.writeMap(map);
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(contentStream.toByteArray()), address)).sync();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log4jHelper.logError(this.getClass(), "SendKRPCError:" + ex.getMessage());
        } finally {
            if (bencodeStream != null) {
                try {
                    bencodeStream.close();
                } catch (Exception ex) {
                }
            }
            if (contentStream != null) {
                try {
                    contentStream.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     *
     */
    private class PacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
            ByteBuf contentByteBuf = datagramPacket.content();
            byte[] contentByteArray = new byte[contentByteBuf.readableBytes()];
            contentByteBuf.readBytes(contentByteArray);
            ByteArrayInputStream contentStream = new ByteArrayInputStream(contentByteArray);
            BencodingInputStream bencodeStream = new BencodingInputStream(contentStream);
            try {
                Map<String, ?> map = bencodeStream.readMap();
                if (map != null) {
                    packetProcessing(datagramPacket.sender(), map);
                }
            } catch (EOFException ex) {
                Log4jHelper.logError(this.getClass(), "ChannelReadError:" + ex.getMessage());
            } finally {
                if (bencodeStream != null) {
                    try {
                        bencodeStream.close();
                    } catch (Exception ex) {
                    }
                }
                if (contentStream != null) {
                    try {
                        contentStream.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}