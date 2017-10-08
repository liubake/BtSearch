package com.erola.btsearch.model;

import java.util.List;

/**
 * 种子信息类
 * Created by Erola on 2017/9/13.
 */
public class TorrentInfo extends BaseModel {
    /**
     *
     */
    private String infoHash;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private long size;
    /**
     *
     */
    private List<TorrentFile> contentFiles;

    public TorrentInfo() {
    }

    public TorrentInfo(String infoHash, String name, long size, List<TorrentFile> contentFiles) {
        this.infoHash = infoHash;
        this.name = name;
        this.size = size;
        this.contentFiles = contentFiles;
    }

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<TorrentFile> getContentFiles() {
        return contentFiles;
    }

    public void setContentFiles(List<TorrentFile> contentFiles) {
        this.contentFiles = contentFiles;
    }

    /**
     * 种子包含的文件信息类
     */
    public static class TorrentFile {
        /**
         *
         */
        private long size;
        /**
         *
         */
        private String path;

        public TorrentFile() {
        }

        public TorrentFile(long size, String path) {
            this.size = size;
            this.path = path;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}