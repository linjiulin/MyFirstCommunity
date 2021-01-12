package com.lingerlin.community.community.dao;
import com.lingerlin.community.community.model.UserMongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author lingerlin
 */
@Repository
public class MongoUserDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public UserMongo findByAccountId(String accountId, String name) {
        Query query = new Query(Criteria.where("accountId").is(accountId).and("name").is(name));
        return mongoTemplate.findOne(query,UserMongo.class, "user");
    }

    public void updateByAccountId(String token, long currentTimeMillis, String avatar_url, String bio, String account_id) {
        Query query = new Query(Criteria.where("accountId").is(account_id));
        Update update = new Update();
        update.set("token", token);
        update.set("gmtModified", currentTimeMillis);
        update.set("avatar", avatar_url);
        update.set("bio", bio);
        mongoTemplate.findAndModify(query, update, UserMongo.class, "user");
    }
    public void insert(UserMongo user) {
        System.out.println(user);
        mongoTemplate.save(user, "user");
    }
    public UserMongo findByToken(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        return mongoTemplate.findOne(query, UserMongo.class, "user");
    }
    public UserMongo findById(String creator) {
        Query query = new Query(Criteria.where("_id").is(creator));
        return mongoTemplate.findOne(query, UserMongo.class, "user");
    }
}
