package com.erola.btsearch.spider.dht.handler;

import com.erola.btsearch.spider.dht.listener.OnTorrentDownloadListener;
import com.erola.btsearch.spider.dht.model.DownloadPeer;
import com.erola.btsearch.spider.dht.util.ByteUtil;
import com.erola.btsearch.util.log4j.Log4jHelper;
import com.erola.btsearch.util.redis.RedisHelper;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Erola on 2017/9/11.
 */
public class PeerDownloadHandler implements Runnable {
    /**
     * 节点Id
     */
    private byte[] nodeId;
    /**
     * 下载线程池
     */
    private ExecutorService downloadThreadPool;
    /**
     * 待下载的peer队列
     */
    private BlockingQueue<DownloadPeer> downloadPeerQueue;
    /**
     * 节点连接超时时间
     */
    private int nodeConnectTimeOut;
    /**
     * 节点读写超时时间
     */
    private int nodeReadWriteTimeOut;
    /**
     *
     */
    private OnTorrentDownloadListener onTorrentDownloadListener;

    /**
     *
     * @param nodeId
     * @param threadPoolSize
     * @param queueCapacity
     */
    public PeerDownloadHandler(byte[] nodeId, int threadPoolSize, int queueCapacity, int nodeConnectTimeOut, int nodeReadWriteTimeOut, OnTorrentDownloadListener onTorrentDownloadListener) {
        this.nodeId=nodeId;
        this.downloadThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.downloadPeerQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.nodeConnectTimeOut = nodeConnectTimeOut;
        this.nodeReadWriteTimeOut = nodeReadWriteTimeOut;
        this.onTorrentDownloadListener = onTorrentDownloadListener;
    }

    /**
     * 添加peer到待下载队列
     *
     * @param peerItem
     */
    public void addDownloadPeer(DownloadPeer peerItem) {
        downloadPeerQueue.add(peerItem);
        //// TODO: 这里可以判断队列是否已满，如果满的话，可以通知DHTServer降低findNode频率
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            try {
                DownloadPeer peerItem = this.downloadPeerQueue.take();
                String peerInfoHashString = ByteUtil.byteArrayToHex(peerItem.getInfo_hash());
                if(!RedisHelper.exists(peerInfoHashString)) {
                    RedisHelper.setex(peerInfoHashString, "Exist", 259200);
                    this.downloadThreadPool.execute(()->{
                        try {
                            PeerWireProtocalHandler peerWireProtocalHandler = new PeerWireProtocalHandler(nodeId, new InetSocketAddress(peerItem.getIp(), peerItem.getPort()), peerItem.getInfo_hash(), nodeConnectTimeOut, nodeReadWriteTimeOut, onTorrentDownloadListener);
                            peerWireProtocalHandler.downloadTorrent();
                        } catch (Exception e) {
                            //出现异常时删除记录，说不定从其它节点可以下载成功呢~
                            RedisHelper.del(peerInfoHashString);
                            if (e instanceof ConnectException) {
                                //忽略网络异常
                            } else if (e instanceof SocketException) {
                                //忽略网络异常
                            } else if (e instanceof SocketTimeoutException) {
                                //忽略网络异常
                            } else if (e instanceof NoRouteToHostException) {
                                //忽略网络异常
                            } else {
                                Log4jHelper.logError(this.getClass(), "PeerDownload error", e);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log4jHelper.logError(this.getClass(), "PeerDownloader threadPool execute error", e);
                continue;
            }
        }
    }
}