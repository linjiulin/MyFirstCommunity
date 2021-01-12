package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dao.MongoDiscussionDao;
import com.lingerlin.community.community.dao.MongoUserDao;
import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.DiscussionMongoDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingerlin
 */
@Service
public class DiscussionServiceMongo {

    @Autowired
    private MongoDiscussionDao mongoDiscussionDao;

    @Autowired
    private MongoUserDao mongoUserDao;

    @Autowired
    private NotificationServiceMongo notificationServiceMongo;

    @Autowired
    private CommentServiceMongo commentServiceMongo;

    public void deleteAllById(String id) {
        //删除讨论
        DiscussionMongo discussionMongo = mongoDiscussionDao.findById(id);
        if(discussionMongo==null){
            throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
        }
        mongoDiscussionDao.deleteById(id);

        //删除通知
        List<NotificationMongo> notificationMongoList = notificationServiceMongo.findByOuterId(id);
        if(notificationMongoList==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notificationServiceMongo.deleteByOuterId(id);

        //删除评论
        List<CommentMongo> commentMongoList = commentServiceMongo.findByDiscussionId(id);
        if(commentMongoList==null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        for(CommentMongo comment:commentMongoList){
            commentServiceMongo.deleteByParentId(comment);
        }
    }


    /**
     * @param search
     * @param page
     * @param size
     * @return
     */
    public PageDTO listAll(String search,Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalCount = 0;
        Integer offset = size *(page-1);
        List<DiscussionMongo> discussionMongoList = new ArrayList<>();
        if(StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search,' ');
            String tagRegex = Arrays.stream(tags).collect(Collectors.joining("|"));
            totalCount = mongoDiscussionDao.countBySearch(tagRegex);
            discussionMongoList = mongoDiscussionDao.listBySearch(tagRegex,offset,size);
        }
        else{
            totalCount = mongoDiscussionDao.count();
            discussionMongoList = mongoDiscussionDao.list(offset,size);
        }
        pageDTO.setPagination(totalCount,page,size);
        if(page<1){
            page=1;
        }
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }
        List<DiscussionMongoDTO> discussionMongoDTOList = new ArrayList<>();
        for (DiscussionMongo discussion : discussionMongoList) {
            UserMongo userMongo = mongoUserDao.findById(discussion.getCreator());
            DiscussionMongoDTO discussionMongoDTO = new DiscussionMongoDTO();
            BeanUtils.copyProperties(discussion,discussionMongoDTO);
            discussionMongoDTO.setUser(userMongo);
            discussionMongoDTOList.add(discussionMongoDTO);
        }
        pageDTO.setData(discussionMongoDTOList);

        return pageDTO;
    }

    /**
     * @param id
     * @param page
     * @param size
     * @return
     */
        public PageDTO list(String id, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalcount = mongoDiscussionDao.countById(id);
        pageDTO.setPagination(totalcount,page,size);
        if(page<1){
            page=1;
        }
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }

        Integer offset = size *(page-1);
        List<DiscussionMongo> discussionMongoList = mongoDiscussionDao.listSelf(id,offset,size);
        List<DiscussionMongoDTO> discussionMongoDTOList = new ArrayList<>();
        for (DiscussionMongo discussion : discussionMongoList) {
            UserMongo userMongo = mongoUserDao.findById(discussion.getCreator());
            DiscussionMongoDTO discussionMongoDTO = new DiscussionMongoDTO();
            BeanUtils.copyProperties(discussion,discussionMongoDTO);
            discussionMongoDTO.setUser(userMongo);
            discussionMongoDTOList.add(discussionMongoDTO);
        }
        pageDTO.setData(discussionMongoDTOList);
        return pageDTO;
    }

    /**
     * @param id 传输的是讨论的id
     * @return 返回的是一个关联了用户的讨论集合
     */
    public DiscussionMongoDTO getById(String id) {
        DiscussionMongo discussionMongo = mongoDiscussionDao.findById(id);
        if(discussionMongo==null){
            throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
        }
        UserMongo userMongo = mongoUserDao.findById(discussionMongo.getCreator());
        DiscussionMongoDTO discussionMongoDTO = new DiscussionMongoDTO();
        BeanUtils.copyProperties(discussionMongo,discussionMongoDTO);
        discussionMongoDTO.setUser(userMongo);
        return discussionMongoDTO;
    }

    /**
     * @param id 传输的讨论id
     * @param title 讨论标题
     * @param description 讨论详细
     * @param tag 讨论标签
     * @param user 传输来的用户
     */
    public void checkDiscussion(String id,String title,String description,String tag,UserMongo user){
        DiscussionMongo discussionMongoOld = mongoDiscussionDao.findById(id);
        if(discussionMongoOld!=null){
            System.out.println("存在讨论");
            discussionMongoOld.setGmtModified(System.currentTimeMillis());
            int updated = mongoDiscussionDao.UpdateById(discussionMongoOld.get_id(),
                    title,
                    description,
                    tag,
                    discussionMongoOld.getGmtModified());
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }
        }
        else {
            System.out.println("不存在讨论");
            DiscussionMongo discussionMongo = new DiscussionMongo();
            discussionMongo.setCommentCount(0);
            discussionMongo.setTitle(title);
            discussionMongo.setDescription(description);
            discussionMongo.setTag(tag);
            discussionMongo.setCreator(user.get_id());
            discussionMongo.setGmtCreate(System.currentTimeMillis());
            discussionMongo.setGmtModified(discussionMongo.getGmtCreate());
            mongoDiscussionDao.create(discussionMongo);
        }
        
    }

    public void incView(String id) {
        DiscussionMongo discussionMongo = mongoDiscussionDao.findById(id);
        System.out.println("阅读数是："+discussionMongo.getViewCount());
        mongoDiscussionDao.UpdateViewById(id);
    }

    public List<DiscussionMongo> getRelatedDiscussionBytag(DiscussionMongoDTO discussionMongoDTO) {
        if(StringUtils.isBlank(discussionMongoDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(discussionMongoDTO.getTag(),',');
        String tagRegex = Arrays.stream(tags).collect(Collectors.joining("|"));

        System.out.println("正则化后的tag："+tagRegex);
        List<DiscussionMongo> discussionMongoList = mongoDiscussionDao.getRelatedDiscussionBytag(discussionMongoDTO.get_id(),tagRegex);
        return discussionMongoList;
    }
}
