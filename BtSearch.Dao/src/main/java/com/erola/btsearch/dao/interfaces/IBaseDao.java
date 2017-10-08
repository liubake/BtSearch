package com.erola.btsearch.dao.interfaces;

import com.erola.btsearch.model.BaseModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

/**
 * Created by Erola on 17/9/15.
 */
public interface IBaseDao<ModelType extends BaseModel> {

    /**
     * 保存单条记录
     * @param model
     */
    void insert(ModelType model);

    /**
     * 修改整个对象
     * @param model
     */
    void updateWhole(ModelType model);

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    ModelType getById(ObjectId id);

    /**
     * 查询所有记录数
     * @return
     */
    long countAll();

    /**
     * 查询符合条件的记录数
     * @param query
     * @return
     */
    long count(Query query);

    /**
     * 返回表中所有记录
     * @return
     */
    List<ModelType> findAll();

    /**
     * 返回满足查询条件的第一条记录
     * @param query
     * @return
     */
    ModelType findOne(Query query);

    /**
     * 返回满足查询条件的所有记录
     * @param query
     * @return
     */
    List<ModelType> findMany(Query query);
}