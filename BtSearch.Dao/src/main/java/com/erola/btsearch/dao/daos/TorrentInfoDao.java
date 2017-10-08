package com.erola.btsearch.dao.daos;

import com.erola.btsearch.dao.interfaces.ITorrentInfoDao;
import com.erola.btsearch.model.TorrentInfo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Erola on 17/9/15.
 */
@Repository("TorrentInfoDao")
public class TorrentInfoDao extends BaseDao<TorrentInfo> implements ITorrentInfoDao {
    /**
     * 根据infoHash查询对应的记录
     * @param infoHash
     * @return
     */
    @Override
    public TorrentInfo getByInfoHash(String infoHash){
        return super.findOne(new Query(Criteria.where("infoHash").is(infoHash)));
    }
}