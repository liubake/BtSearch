package com.erola.btsearch.spider.dht.server;

import com.erola.btsearch.model.TorrentInfo;
import com.erola.btsearch.spider.dht.listener.OnTorrentDownloadListener;
import com.erola.btsearch.spider.dht.util.ByteUtil;
import com.erola.btsearch.util.json.JsonHelper;
import com.erola.btsearch.util.log4j.Log4jHelper;
import org.ardverk.coding.BencodingInputStream;
import org.ardverk.coding.BencodingOutputStream;
import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

/**
 * Created by Erola on 2017/9/11.
 */
public class PeerWireProtocalHandler {
    private final static byte BT_MSG_ID = 20 & 0xff;
    private final static int MAX_METADATA_SIZE = 1000000;
    private final static String BT_PROTOCOL_SIGN = "BitTorrent protocol";
    private final static String BEP009_PROTOCOL_SIGN = "m";
    private final static int EXT_HANDSHAKE_ID = 0;
    private final static byte[] EXT_HANDSHAKE_DATA = "d1:md11:ut_metadatai1eee".getBytes();
    private final static byte[] BT_PROTOCOL_RESERVED = new byte[]{(byte) (0x00 & 0xff), (byte) (0x00 & 0xff),
            (byte) (0x00 & 0xff), (byte) (0x00 & 0xff), (byte) (0x00 & 0xff), (byte) (0x10 & 0xff),
            (byte) (0x00 & 0xff), (byte) (0x01 & 0xff),};

    private int connectTimeOut = 3000;
    private int readWriteTimeOut = 5000;
    private byte[] peerInfohash;
    private InetSocketAddress peerAddress;
    private int utMetadata;
    private byte[] metadata;
    private int metadataSize;
    private int nextBuffSize;
    private boolean stopDownload;
    private INextProcess nextProcess;
    private boolean[] piecesDownloadSign;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private PipedInputStream pipedInputStream;
    private PipedOutputStream pipedOutputStream;
    private BufferedOutputStream bufferedOutputStream;
    private byte[] peerId = ByteUtil.createRandomNodeId();
    private OnTorrentDownloadListener onTorrentDownloadListener;

    /**
     *
     * @param peerId
     * @param address
     * @param peerInfoHash
     * @param connectTimeOut
     * @param readWriteTimeOut
     * @param onTorrentDownloadListener
     */
    public PeerWireProtocalHandler(byte[] peerId, InetSocketAddress address, byte[] peerInfoHash, int connectTimeOut, int readWriteTimeOut, OnTorrentDownloadListener onTorrentDownloadListener) {
        this.peerId = peerId;
        this.peerAddress = address;
        this.peerInfohash = peerInfoHash;
        this.connectTimeOut = connectTimeOut;
        this.readWriteTimeOut = readWriteTimeOut;
        this.onTorrentDownloadListener = onTorrentDownloadListener;
    }

    /**
     * @throws Exception
     */
    public void downloadTorrent() throws Exception {
        socket = new Socket();
        socket.setReuseAddress(true);
        socket.setSoTimeout(readWriteTimeOut);
        socket.connect(peerAddress, connectTimeOut);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream(22 * 1024);
        pipedInputStream.connect(pipedOutputStream);
        bufferedOutputStream = new BufferedOutputStream(pipedOutputStream);
        peerWireCommunicate();
    }

    /**
     * @throws IOException
     */
    private void peerWireCommunicate() throws Exception {
        parseNext(1, (byte[] buffFirst) -> {
            final int protocolLength = (int) buffFirst[0];
            parseNext(protocolLength + 48, (byte[] buffSecond) -> {
                byte[] protocolBuff = Arrays.copyOfRange(buffSecond, 0, protocolLength);
                if (!BT_PROTOCOL_SIGN.equals(new String(protocolBuff))) {
                    stopDownloadAndRelease(1);
                    throw new Exception("handshake failed.");
                } else {
                    byte[] handShakeBuff = Arrays.copyOfRange(buffSecond, protocolLength, buffSecond.length);
                    if (handShakeBuff[5] != 0x10) {
                        stopDownloadAndRelease(2);
                        throw new Exception("remote peer don't support download metadata.");
                    } else {
                        parseNext(4, onPieceLengthProcess);
                        sendMessage(EXT_HANDSHAKE_ID, EXT_HANDSHAKE_DATA);
                    }
                }
            });
        });
        outputStream.write(BT_PROTOCOL_SIGN.length() & 0xff);
        outputStream.write(BT_PROTOCOL_SIGN.getBytes());
        outputStream.write(BT_PROTOCOL_RESERVED);
        outputStream.write(peerInfohash);
        outputStream.write(peerId);
        outputStream.flush();
        int buffLength = -1;
        byte[] readBuff = new byte[1024];
        while (!stopDownload && !socket.isClosed() && (buffLength = inputStream.read(readBuff)) != -1) {
            bufferedOutputStream.write(readBuff, 0, buffLength);
            bufferedOutputStream.flush();
            while (!stopDownload && pipedInputStream != null && pipedInputStream.available() >= nextBuffSize) {
                byte[] buff = new byte[nextBuffSize];
                pipedInputStream.read(buff, 0, nextBuffSize);
                nextProcess.onProcess(buff);
            }
        }
    }

    /**
     * 下一个处理流程
     *
     * @param nextSize
     * @param nextProcess
     */
    private void parseNext(int nextSize, INextProcess nextProcess) {
        this.nextBuffSize = nextSize;
        this.nextProcess = nextProcess;
    }

    /**
     * 请求 piece
     *
     * @throws IOException
     */
    private void requestPieces() throws IOException {
        metadata = new byte[metadataSize];
        int piecesCount = (int) Math.ceil(metadataSize / (16.0 * 1024));
        piecesDownloadSign = new boolean[piecesCount];
        for (int i = 0; i < piecesCount; i++) {
            Map<String, Object> pieceContentMap = new HashMap<>();
            pieceContentMap.put("msg_type", 0);
            pieceContentMap.put("piece", i);
            sendMessage(utMetadata, encodeMapToBytes(pieceContentMap));
        }
    }

    /**
     * 当收到 piece 时
     *
     * @param buff
     * @throws IOException
     */
    private void onReceivePiece(byte[] buff) throws Exception {
        String pieceContent = new String(buff, "ISO-8859-1");
        int position = pieceContent.indexOf("ee") + 2;
        Map<String, Object> pieceMap = decodeBytesToMap(pieceContent.substring(0, position).getBytes("ISO-8859-1"));
        if (!pieceMap.containsKey("msg_type") || !pieceMap.containsKey("piece")) {
            stopDownloadAndRelease(3);
            throw new Exception("onPiece packet error.");
        } else if (((BigInteger) pieceMap.get("msg_type")).intValue() != 1) {
            stopDownloadAndRelease(4);
            throw new Exception("onPiece error, msg_type:" + pieceMap.get("msg_type"));
        } else {
            int piece = ((BigInteger) pieceMap.get("piece")).intValue();
            byte[] pieceMetadata = Arrays.copyOfRange(buff, position, buff.length);
            System.arraycopy(pieceMetadata, 0, metadata, piece * 16 * 1024, pieceMetadata.length);
            piecesDownloadSign[piece] = true;
            checkDownloadComplete();
        }
    }

    /**
     * 检测是否下载完成
     *
     * @throws Exception
     */
    private void checkDownloadComplete() throws Exception {
        boolean downloadComplete = true;
        for (boolean item : piecesDownloadSign) {
            if (!item) {
                downloadComplete = false;
                break;
            }
        }
        if (downloadComplete) {
            stopDownload = true;
            Map<String, Object> torrentMap = decodeBytesToMap(metadata);
            if (torrentMap != null) {
                Map<String, Object> torrentInfoMap = torrentMap.containsKey("info") ? (Map<String, Object>) torrentMap.get("info") : torrentMap;
                if (torrentInfoMap.containsKey("name")) {
                    String encoding = "UTF-8";
                    if (torrentMap.containsKey("encoding")) {
                        encoding = (String) torrentMap.get("encoding");
                    }
                    Long torrentLength = new Long(0);
                    String torrentName = decodeTorrentMapItem(torrentMap, "name", encoding);
                    List<TorrentInfo.TorrentFile> torrentFiles = new ArrayList<TorrentInfo.TorrentFile>();
                    if (torrentInfoMap.containsKey("files")) {
                        List<Map<String, Object>> torrentFilesMap = (List<Map<String, Object>>) torrentInfoMap.get("files");
                        for (Map<String, Object> item : torrentFilesMap) {
                            String filePath = decodeTorrentMapArrayItem(item, "path", encoding);
                            if (filePath.indexOf("if you see this file") == -1 && filePath.indexOf("如果您看到此文件") == -1) {
                                Long fileLength = ((BigInteger) item.get("length")).longValue();
                                torrentLength += fileLength;
                                torrentFiles.add(new TorrentInfo.TorrentFile(fileLength, filePath));
                            }
                        }
                    } else {
                        torrentLength = ((BigInteger) torrentMap.get("length")).longValue();
                    }
                    TorrentInfo torrentInfo = new TorrentInfo(ByteUtil.byteArrayToHex(peerInfohash), torrentName, torrentLength, torrentFiles);
                    if (onTorrentDownloadListener != null) {
                        onTorrentDownloadListener.saveOrUpdateTorrentInfo(torrentInfo);
                    }
                    Log4jHelper.logDebug(this.getClass(), "TorrentDownload:"+ JsonHelper.objectToJson(torrentInfo));
                } else {
                    throw new Exception("On downloadComplete torrentMap dosen't containsKey [name].");
                }
            } else {
                throw new Exception("On downloadComplete torrentMap is null.");
            }
        }
    }

    /**
     * 发送信息
     *
     * @param id
     * @param data
     * @throws IOException
     */
    private void sendMessage(int id, byte[] data) throws IOException {
        byte[] length_prefix = ByteUtil.intToByteArray(data.length + 2);
        for (int i = 0; i < 4; i++) {
            length_prefix[i] = (byte) (length_prefix[i] & 0xff);
        }
        outputStream.write(length_prefix);
        outputStream.write(BT_MSG_ID);
        outputStream.write((byte) (id & 0xff));
        outputStream.write(data);
        outputStream.flush();
    }

    /**
     * 当收到 PieceLength 包时
     */
    private INextProcess onPieceLengthProcess = new INextProcess() {
        @Override
        public void onProcess(byte[] buff) {
            int length = ByteUtil.byteArrayToInt(buff);
            if (length <= 0) {
                parseNext(4, onPieceLengthProcess);
            } else {
                parseNext(length, onPieceContentProcess);
            }
        }
    };

    /**
     * 当收到 PieceContent 包时
     */
    private INextProcess onPieceContentProcess = new INextProcess() {
        @Override
        public void onProcess(byte[] buff) throws Exception {
            parseNext(4, onPieceLengthProcess);
            if (buff[0] == PeerWireProtocalHandler.BT_MSG_ID) {
                byte[] contentBuff = Arrays.copyOfRange(buff, 2, buff.length);
                if (buff[1] == 0) {
                    Map<String, Object> contentMap = decodeBytesToMap(contentBuff);
                    Map<String, Object> bep009Map = (Map<String, Object>) contentMap.get(BEP009_PROTOCOL_SIGN);
                    if (bep009Map == null || !bep009Map.containsKey("ut_metadata") || !contentMap.containsKey("metadata_size")) {
                        stopDownloadAndRelease(5);
                        throw new Exception("onExtendHandShake failed.");
                    } else {
                        PeerWireProtocalHandler.this.utMetadata = ((BigInteger) bep009Map.get("ut_metadata")).intValue();
                        PeerWireProtocalHandler.this.metadataSize = ((BigInteger) contentMap.get("metadata_size")).intValue();
                        if (PeerWireProtocalHandler.this.metadataSize > MAX_METADATA_SIZE) {
                            stopDownloadAndRelease(6);
                            throw new Exception("metadataSize more than MAX_METADATA_SIZE.");
                        } else {
                            requestPieces();
                        }
                    }
                } else {
                    onReceivePiece(contentBuff);
                }
            }
        }
    };

    /**
     * @param map
     * @param key
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    private String decodeTorrentMapItem(Map<String, Object> map, String key, String encoding)
            throws UnsupportedEncodingException {
        return map.containsKey(key + ".utf-8") ? new String((byte[]) map.get(key + ".utf-8"), "UTF-8")
                : new String((byte[]) map.get(key), encoding);
    }

    /**
     * @param map
     * @param key
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    private String decodeTorrentMapArrayItem(Map<String, Object> map, String key, String encoding)
            throws UnsupportedEncodingException {
        return map.containsKey(key + ".utf-8") ? new String(((List<byte[]>) map.get(key + ".utf-8")).get(0), "UTF-8")
                : new String(((List<byte[]>) map.get(key)).get(0), encoding);
    }

    /**
     * 将 byte[] 转换为 Map<String, Object>
     *
     * @param buff
     * @return
     * @throws IOException
     */
    private Map<String, Object> decodeBytesToMap(byte[] buff) throws IOException {
        Map<String, Object> ret;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(buff);
             BencodingInputStream bencode = new BencodingInputStream(stream)) {
            ret = (Map<String, Object>) bencode.readMap();
        } catch (IOException ex) {
            stopDownloadAndRelease(7);
            throw ex;
        }
        return ret;
    }

    /**
     * 将 Map<String, Object> 转换为 byte[]
     *
     * @param map
     * @return
     * @throws IOException
     */
    private byte[] encodeMapToBytes(Map<String, Object> map) throws IOException {
        byte[] ret = {};
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             BencodingOutputStream bencode = new BencodingOutputStream(stream)) {
            bencode.writeMap(map);
            ret = stream.toByteArray();
        } catch (IOException ex) {
            stopDownloadAndRelease(8);
            throw ex;
        }
        return ret;
    }

    /**
     * 停止下载并释放资源
     */
    private void stopDownloadAndRelease(int index) {
        stopDownload = true;
        try {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
                bufferedOutputStream = null;
            }
        } catch (IOException e) {
        }
        try {
            if (pipedInputStream != null) {
                pipedInputStream.close();
                pipedInputStream = null;
            }
        } catch (IOException e) {
        }
        try {
            if (pipedOutputStream != null) {
                pipedOutputStream.close();
                pipedOutputStream = null;
            }
        } catch (IOException e) {
        }
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException e) {
        }
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
        }
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
        }
    }

    /**
     *
     */
    protected void finalize() {
        stopDownloadAndRelease(9);
    }

    /**
     * 下个处理流程
     */
    private interface INextProcess {
        void onProcess(byte[] buff) throws Exception;
    }
}