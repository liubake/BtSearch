package com.erola.btsearch.spider.dht.model;

import java.util.Arrays;

/**
 * Created by Erola on 2017/9/12.
 */
public class Node {
    private String ip;
    private int port;
    private byte[] nodeId;

    public Node(String ip, int port, byte[] nodeId) {
        super();
        this.ip = ip;
        this.port = port;
        this.nodeId = Arrays.copyOfRange(nodeId, 0, nodeId.length);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getNodeId() {
        return nodeId;
    }

    public void setNodeId(byte[] nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ((Node) obj).getIp().equals(ip);
    }
}