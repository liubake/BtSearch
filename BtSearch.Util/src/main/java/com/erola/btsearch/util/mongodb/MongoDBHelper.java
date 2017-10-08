package com.erola.btsearch.util.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

/**
 * MongoDB 操作辅助类
 * Created by Erola on 2017/9/10.
 */
public class MongoDBHelper {
    /**
     *
     */
    private static class MongoTemplateHelper{
        /**
         *
         */
        private static final MongoTemplate instance =  initMongoTemplate();

        /**
         *
         * @return
         */
        private static MongoTemplate initMongoTemplate(){
            MongoClient mongoClient=new MongoClient(new ServerAddress(MongoDBConfig.getServerAddress(), MongoDBConfig.getPort()),
                    MongoClientOptions.builder().connectionsPerHost(MongoDBConfig.getPoolSize()).threadsAllowedToBlockForConnectionMultiplier(MongoDBConfig.getBlockSize())
                            .connectTimeout(MongoDBConfig.getConnectTimeout()).maxConnectionIdleTime(MongoDBConfig.getConnectIdletime())
                            .maxWaitTime(MongoDBConfig.getBlockWaittime()).build());
            return new MongoTemplate(mongoClient, MongoDBConfig.getDbName());
        }
    }

    /**
     *
     * @param collectionName
     * @param document
     * @param <TDocument>
     */
    public static <TDocument> void insert(String collectionName,TDocument document){
        MongoTemplateHelper.instance.insert(document, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documents
     * @param <TDocument>
     */
    public static <TDocument> void insert(String collectionName,List<TDocument> documents){
        MongoTemplateHelper.instance.insert(documents, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param document
     * @param <TDocument>
     */
    public static <TDocument> void updateWhole(String collectionName,TDocument document){
        MongoTemplateHelper.instance.save(document, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documents
     * @param <TDocument>
     */
    public static <TDocument> void updateWhole(String collectionName,List<TDocument> documents){
        MongoTemplateHelper.instance.save(documents, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documentClass
     * @param query
     * @param <TDocument>
     * @return
     */
    public static <TDocument> long count(String collectionName, Class<TDocument> documentClass ,Query query){
        return MongoTemplateHelper.instance.count(query, documentClass, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documentClass
     * @param <TDocument>
     * @return
     */
    public static <TDocument> List<TDocument> findAll(String collectionName, Class<TDocument> documentClass){
        return MongoTemplateHelper.instance.findAll(documentClass, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documentClass
     * @param query
     * @param <TDocument>
     * @return
     */
    public static <TDocument> TDocument findOne(String collectionName, Class<TDocument> documentClass ,Query query){
        return MongoTemplateHelper.instance.findOne(query, documentClass, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documentClass
     * @param query
     * @param <TDocument>
     * @return
     */
    public static <TDocument> List<TDocument> findMany(String collectionName, Class<TDocument> documentClass,Query query){
        return MongoTemplateHelper.instance.find(query, documentClass, collectionName);
    }

    /**
     *
     * @param collectionName
     * @param documentClass
     * @param query
     * @param <TDocument>
     * @return
     */
    public static <TDocument> WriteResult remove(String collectionName, Class<TDocument> documentClass , Query query){
        return MongoTemplateHelper.instance.remove(query, documentClass, collectionName);
    }
}