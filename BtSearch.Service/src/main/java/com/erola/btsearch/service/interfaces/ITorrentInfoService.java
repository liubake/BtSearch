package com.erola.btsearch.service.interfaces;

import com.erola.btsearch.dao.interfaces.ITorrentInfoDao;
import com.erola.btsearch.model.TorrentInfo;

/**
 * Created by Erola on 17/9/15.
 */
public interface ITorrentInfoService extends IBaseService<TorrentInfo, ITorrentInfoDao> {

    /**
     * 根据infoHash查询对应的记录
     * @param infoHash
     * @return
     */
    TorrentInfo getByInfoHash(String infoHash);

    /**
     * 保存或更新TorrentInfo
     * @param torrentInfo
     */
    void saveOrUpdate(TorrentInfo torrentInfo);
}