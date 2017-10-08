package com.erola.btsearch.service.services;

import com.erola.btsearch.dao.interfaces.ITorrentInfoDao;
import com.erola.btsearch.model.TorrentInfo;
import com.erola.btsearch.service.interfaces.ITorrentInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by Erola on 17/9/15.
 */
@Service("TorrentInfoService")
public class TorrentInfoService extends BaseService<TorrentInfo, ITorrentInfoDao> implements ITorrentInfoService {
    /**
     * @param dao
     */
    public TorrentInfoService(@Qualifier("TorrentInfoDao")ITorrentInfoDao dao) {
        super(dao);
    }

    /**
     * 根据infoHash查询对应的种子信息
     * @param infoHash
     * @return
     */
    @Override
    public TorrentInfo getByInfoHash(String infoHash){
        return entityDao.getByInfoHash(infoHash);
    }

    /**
     * 保存或更新TorrentInfo
     * @param torrentInfo
     */
    @Override
    public void saveOrUpdate(TorrentInfo torrentInfo){
        TorrentInfo dbTorrentInfo=getByInfoHash(torrentInfo.getInfoHash());
        if(dbTorrentInfo==null){
            super.insert(torrentInfo);
        }else{
            //如果已经存在对应的种子信息，则更新种子信息的最后更新时间，作为种子活跃度的参考
            super.updateWhole(dbTorrentInfo);
        }
    }
}