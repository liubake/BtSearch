package com.erola.btsearch.spider.dht.listener;

import com.erola.btsearch.model.TorrentInfo;

/**
 * Created by Erola on 2017/9/13.
 */
public interface OnTorrentDownloadListener {
    /**
     *
     * @param torrentInfo
     */
    public void saveOrUpdateTorrentInfo(TorrentInfo torrentInfo);
}