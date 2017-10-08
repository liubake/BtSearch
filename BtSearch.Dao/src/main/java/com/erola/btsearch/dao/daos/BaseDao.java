package com.erola.btsearch.dao.daos;

import com.erola.btsearch.dao.interfaces.IBaseDao;
import com.erola.btsearch.model.BaseModel;
import com.erola.btsearch.util.mongodb.MongoDBHelper;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

/**
 * Created by Erola on 17/9/15.
 */
public abstract class BaseDao<ModelType extends BaseModel> implements IBaseDao<ModelType> {
    /**
     *
     */
    protected final Class<ModelType> modelClass;

    /**
     *
     */
    public BaseDao(){
        this.modelClass=(Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     *
     */
    protected String getTableName(){
        return this.modelClass.getSimpleName().toLowerCase();
    }

    /**
     * 保存对象，以对应类型名为表名
     * @param model
     */
    @Override
    public void insert(ModelType model){
        if(model.get_id()==null){
            model.set_id(ObjectId.get());
        }
        model.setCreateTime(new Date());
        MongoDBHelper.insert(getTableName(), model);
    }

    /**
     * 修改整个对象
     */
    @Override
    public void updateWhole(ModelType model){
        if(model.get_id()==null){
            throw new RuntimeException("un persistent model!");
        }
        model.setUpdateTime(new Date());
        MongoDBHelper.updateWhole(getTableName(), model);
    }

    /**
     * 根据id查询对象
     */
    @Override
    public ModelType getById(ObjectId id){
        if(id==null){
            throw new RuntimeException("id can not be null!");
        }
        return MongoDBHelper.findOne(getTableName(), this.modelClass, new Query(Criteria.where("_id").is(id)));
    }

    /**
     * 查询所有记录数
     * @return
     */
    @Override
    public long countAll(){
        return count(new Query());
    }

    /**
     * 查询符合条件的记录数
     * @param query
     * @return
     */
    @Override
    public long count(Query query){
        return MongoDBHelper.count(getTableName(), this.modelClass, query);
    }

    /**
     * 返回表中所有记录
     * @return
     */
    @Override
    public List<ModelType> findAll(){
        return MongoDBHelper.findAll(getTableName(), this.modelClass);
    }

    /**
     * 返回满足查询条件的第一条记录
     * @param query
     * @return
     */
    @Override
    public ModelType findOne(Query query){
        return MongoDBHelper.findOne(getTableName(), this.modelClass, query);
    }

    /**
     * 返回满足查询条件的所有记录
     * @param query
     * @return
     */
    @Override
    public List<ModelType> findMany(Query query){
        return MongoDBHelper.findMany(getTableName(), this.modelClass, query);
    }
}