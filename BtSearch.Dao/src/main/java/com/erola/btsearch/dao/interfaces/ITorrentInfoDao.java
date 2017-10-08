package com.erola.btsearch.dao.interfaces;

import com.erola.btsearch.model.TorrentInfo;

/**
 * Created by Erola on 17/9/15.
 */
public interface ITorrentInfoDao extends IBaseDao<TorrentInfo> {
    /**
     * 根据infoHash查询对应的记录
     * @param infoHash
     * @return
     */
    TorrentInfo getByInfoHash(String infoHash);
}