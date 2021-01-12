package com.lingerlin.community.community.dao;

import com.lingerlin.community.community.model.CommentMongo;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
/**
 * @author lingerlin
 */
@Repository
public class MongoCommentDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void deleteByParentId(CommentMongo comment) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria.orOperator(Criteria.where("parentId").is(comment.getParentId()),Criteria.where("parentId").is(comment.get_id())));
    }

    public List<CommentMongo> listByParentId(String id,Integer type) {
        Query query = new Query(Criteria.where("parentId").is(id).and("type").is(type));
        return mongoTemplate.find(query, CommentMongo.class, "comment");
    }

    public CommentMongo selectById(String parentId) {
        Query query = new Query(Criteria.where("_id").is(parentId));
        return mongoTemplate.findOne(query, CommentMongo.class, "comment");
    }
    public void insert(CommentMongo comment) {
        mongoTemplate.save(comment, "comment");
    }

    public void updateCommentCountById(String parentId) {
        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = new Update();
        update.inc("commentCount", 1);
        //update.set("gmtModified", new Date().getTime());
        mongoTemplate.findAndModify(query, update, CommentMongo.class, "comment");
    }
}
