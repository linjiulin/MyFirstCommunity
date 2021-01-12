package com.lingerlin.community.community.dao;

import com.lingerlin.community.community.model.NotificationMongo;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lingerlin
 */
@Repository
public class MongoNotificationDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public Integer countByReceiver(String receiver) {
        Query query = new Query(Criteria.where("receiver").is(receiver));
        return (int) mongoTemplate.count(query, NotificationMongo.class, "notification");
    }
    public List<NotificationMongo> selectByOuterId(String outerId) {
        Query query = new Query(Criteria.where("outerId").is(outerId));
        return mongoTemplate.find(query, NotificationMongo.class, "notification");
    }
    public void deleteByOuterId(String outerId) {
        Query query = new Query(Criteria.where("outerId").is(outerId));
        mongoTemplate.findAllAndRemove(query, NotificationMongo.class, "notification");
    }
    public void insert(NotificationMongo notification) {
        mongoTemplate.save(notification, "notification");
    }
    public List<NotificationMongo> listSelf(String receiver, Integer offset, Integer size) {
        Query query = new Query(Criteria.where("receiver").is(receiver));
        query.skip(offset);
        query.limit(size);
        query.with(Sort.by(Sort.Order.desc("gmtCreate")));
        return mongoTemplate.find(query, NotificationMongo.class, "notification");
    }
    public NotificationMongo selectById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, NotificationMongo.class, "notification");
    }

    public void updateStatusById(NotificationMongo notification) {
        mongoTemplate.save(notification, "notification");
    }
    public Integer unreadCountByReceiver(String receiver, int status) {
        Query query = new Query(Criteria.where("receiver").is(receiver).and("status").is(status));
        return (int) mongoTemplate.count(query, NotificationMongo.class, "notification");
    }
}
