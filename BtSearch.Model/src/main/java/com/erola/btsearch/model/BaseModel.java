package com.erola.btsearch.model;

import org.bson.types.ObjectId;
import java.util.Date;

/**
 * Created by Erola on 2017/9/13.
 */
public abstract class BaseModel {
    /**
     *
     */
    private ObjectId _id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后一次修改时间
     */
    private Date updateTime;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}