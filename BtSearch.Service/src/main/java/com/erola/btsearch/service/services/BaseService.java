package com.erola.btsearch.service.services;

import com.erola.btsearch.dao.interfaces.IBaseDao;
import com.erola.btsearch.model.BaseModel;
import com.erola.btsearch.service.interfaces.IBaseService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

/**
 * Created by Erola on 17/9/15.
 */
public class BaseService<ModelType extends BaseModel, IDaoType extends IBaseDao<ModelType>> implements IBaseService<ModelType, IDaoType> {

    /**
     *
     */
    protected final IDaoType entityDao;

    /**
     *
     * @param dao
     */
    public BaseService(IDaoType dao) {
        this.entityDao=dao;
    }

    /**
     * 保存对象，以对应类型名为表名
     * @param model
     */
    @Override
    public void insert(ModelType model) {
        entityDao.insert(model);
    }

    /**
     * 修改整个对象
     * @param model
     */
    @Override
    public void updateWhole(ModelType model){
        entityDao.updateWhole(model);
    }

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public ModelType getById(ObjectId id){
        return entityDao.getById(id);
    }

    /**
     * 查询所有记录数
     * @return
     */
    @Override
    public long countAll(){
        return entityDao.countAll();
    }

    /**
     * 查询符合条件的记录数
     * @param query
     * @return
     */
    @Override
    public long count(Query query) {
        return entityDao.count(query);
    }

    /**
     * 返回表中所有记录
     * @return
     */
    @Override
    public List<ModelType> findAll() {
        return entityDao.findAll();
    }

    /**
     * 返回满足查询条件的第一条记录
     * @param query
     * @return
     */
    @Override
    public ModelType findOne(Query query) {
        return entityDao.findOne(query);
    }

    /**
     * 返回满足查询条件的所有记录
     * @param query
     * @return
     */
    @Override
    public List<ModelType> findMany(Query query) {
        return entityDao.findMany(query);
    }
}