package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dao.MongoCommentDao;
import com.lingerlin.community.community.dao.MongoDiscussionDao;
import com.lingerlin.community.community.dao.MongoNotificationDao;
import com.lingerlin.community.community.dao.MongoUserDao;
import com.lingerlin.community.community.dto.CommentDTO;
import com.lingerlin.community.community.dto.CommentMongoDTO;
import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.enums.NotificationStatusEnum;
import com.lingerlin.community.community.enums.NotificationTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceMongo {

    @Autowired
    private MongoCommentDao mongoCommentDao;

    @Autowired
    private MongoDiscussionDao mongoDiscussionDao;

    @Autowired
    private MongoUserDao mongoUserDao;

    @Autowired
    private MongoNotificationDao mongoNotificationDao;


    @Transactional
    public void insert(CommentMongo comment,UserMongo commentator) {
        if(comment.getParentId()==null){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            //回复评论

            //获取到所要回复的评论
            CommentMongo dbComment = mongoCommentDao.selectById(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //获取到当前回复所在的讨论
            DiscussionMongo discussionMongo = mongoDiscussionDao.findById(dbComment.getParentId());
            if(discussionMongo==null){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }

            //进行回复
            mongoCommentDao.insert(comment);
            //增加回复数
            mongoCommentDao.updateCommentCountById(comment.getParentId());
            //进行通知
            createNotify(comment,dbComment.getCommentator(),commentator.getName(),discussionMongo.getTitle(), NotificationTypeEnum.REPLY_COMMENT,discussionMongo.get_id());
        }
        else{
            //回复讨论
            DiscussionMongo discussionMongo = mongoDiscussionDao.findById(comment.getParentId());
            if(discussionMongo == null){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }
            mongoCommentDao.insert(comment);
            //增加回复数
            mongoDiscussionDao.updateCommentCountById(comment.getParentId());
            //进行通知
            createNotify(comment,discussionMongo.getCreator(),commentator.getName(),discussionMongo.getTitle(),NotificationTypeEnum.REPLY_DISCUSSION,discussionMongo.get_id());
        }
    }
    private void createNotify(CommentMongo comment,String receiver,String notifierName,String outerTitle,NotificationTypeEnum notificationTypeEnum,String outerId){
        NotificationMongo notification = new NotificationMongo();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        mongoNotificationDao.insert(notification);
    }

    public List<CommentMongoDTO> listByParentId(String id, Integer type) {
        List<CommentMongo> comments = mongoCommentDao.listByParentId(id,type);
        System.out.println("查询出来的Comment为:"+comments);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        List<CommentMongoDTO> commentMongoDTOList = new ArrayList<>();
        for(CommentMongo comment :comments){
            UserMongo user = mongoUserDao.findById(comment.getCommentator());
            CommentMongoDTO commentMongoDTO = new CommentMongoDTO();
            BeanUtils.copyProperties(comment,commentMongoDTO);
            commentMongoDTO.setUser(user);
            commentMongoDTOList.add(commentMongoDTO);
        }
//        userMapper.findById()
        System.out.println("查询出来的DTO为："+commentMongoDTOList);
        return commentMongoDTOList;
    }

    public List<CommentMongo> findByDiscussionId(String id) {
        List<CommentMongo> commentMongoList = mongoCommentDao.listByParentId(id,CommentTypeEnum.DISCUSSION.getType());
        return commentMongoList;
    }

    public void deleteByParentId(CommentMongo comment) {
        mongoCommentDao.deleteByParentId(comment);
    }
}
