package com.lingerlin.community.community.dao;

import com.lingerlin.community.community.model.DiscussionMongo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author lingerlin
 */
@Repository
public class MongoDiscussionDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public Integer countById(String id) {
        Query query = new Query(Criteria.where("creator").is(id));
        return (int) mongoTemplate.count(query,DiscussionMongo.class,"discussion");
    }

    public void create(DiscussionMongo discussion) {
        mongoTemplate.save(discussion, "discussion");
    }
    public DiscussionMongo findById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, DiscussionMongo.class, "discussion");
    }

    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,DiscussionMongo.class,"discussion");
    }
    public Integer count() {
        return (int) mongoTemplate.count(new Query(), DiscussionMongo.class, "discussion");
    }

    public Integer countBySearch(String tagRegex) {
        Query query = new Query(Criteria.where("tag").regex(tagRegex));
        return (int) mongoTemplate.count(query,DiscussionMongo.class,"discussion");
    }
    public List<DiscussionMongo> listBySearch(String tagRegex, Integer offset, Integer size) {
        Query query = new Query(Criteria.where("title").regex(tagRegex));
        query.skip(offset);
        query.limit(size);
        return mongoTemplate.find(query, DiscussionMongo.class, "discussion");
    }
    public List<DiscussionMongo> list(Integer offset, Integer size) {
        Query query = new Query();
        query.skip(offset);
        query.limit(size);
        query.with(Sort.by(Sort.Order.desc("gmtModified")));
        return mongoTemplate.find(query, DiscussionMongo.class, "discussion");
    }

    public List<DiscussionMongo> listSelf(String id, Integer offset, Integer size) {
        Query query = new Query(Criteria.where("creator").is(id));
        query.skip(offset);
        query.limit(size);
        query.with(Sort.by(Sort.Order.desc("gmtModified")));
        return mongoTemplate.find(query, DiscussionMongo.class, "discussion");
    }
    public int UpdateById(String id, String title, String description, String tag, Long gmtModified) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("title", title);
        update.set("description", description);
        update.set("tag", tag);
        update.set("gmtModified", gmtModified);
        //mongoTemplate.findAndModify(query, update, Discussion.class, "discussion");
        return mongoTemplate.findAndModify(query, update, DiscussionMongo.class, "discussion") == null ? 0 : 1;
    }
    public void UpdateViewById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("viewCount", 1);
        mongoTemplate.findAndModify(query, update, DiscussionMongo.class, "discussion");
    }

    public List<DiscussionMongo> getRelatedDiscussionBytag(String id, String tagRegex) {
        Criteria criteria = new Criteria();
        Query query = new Query();
        query.addCriteria(Criteria.where("tag").regex(tagRegex));
        query.addCriteria(Criteria.where("_id").ne(id));
        return mongoTemplate.find(query, DiscussionMongo.class, "discussion");
    }

    public void updateCommentCountById(String parentId) {
        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = new Update();
        update.inc("commentCount", 1);
        mongoTemplate.findAndModify(query, update, DiscussionMongo.class, "discussion");
    }
}
