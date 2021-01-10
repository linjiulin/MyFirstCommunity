package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.CommentDTO;
import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.enums.NotificationStatusEnum;
import com.lingerlin.community.community.enums.NotificationTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.mapper.CommentMapper;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.NotificationMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Comment;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.Notification;
import com.lingerlin.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator) {
        if(comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            //回复评论

            //获取到所要回复的评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //获取到当前回复所在的讨论
            Discussion discussion = discussionMapper.findById(dbComment.getParentId());
            if(discussion==null){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }

            //进行回复
            commentMapper.insert(comment);
            //增加回复数
            commentMapper.UpdateCommentCountById(comment.getParentId());
            //进行通知
            createNotify(comment,dbComment.getCommentator(),commentator.getName(),discussion.getTitle(), NotificationTypeEnum.REPLY_COMMENT,discussion.getId());
        }
        else{
            //回复讨论
            Discussion discussion = discussionMapper.findById(comment.getParentId());
            if(discussion == null){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加回复数
            discussionMapper.UpdateCommentCountById(comment.getParentId());
            //进行通知
            createNotify(comment,discussion.getCreator(),commentator.getName(),discussion.getTitle(),NotificationTypeEnum.REPLY_DISCUSSION,discussion.getId());
        }
    }
    private void createNotify(Comment comment,Integer receiver,String notifierName,String outerTitle,NotificationTypeEnum notificationTypeEnum,Integer outerId){
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterId(outerId);
        notification.setOuterTitle(outerTitle);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notificationMapper.insert(notification);
    }
    public List<CommentDTO> listByParentId(Integer id,Integer type) {
        List<Comment> comments = commentMapper.listByParentId(id,type);
        System.out.println("查询出来的Comment为:"+comments);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(Comment comment :comments){
            User user = userMapper.findById(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            commentDTOList.add(commentDTO);
        }
//        userMapper.findById()
        System.out.println("查询出来的DTO为："+commentDTOList);
        return commentDTOList;
    }

    public List<Comment> findByDiscussionId(Integer id) {
        List<Comment> commentList = commentMapper.listByParentId(id,CommentTypeEnum.DISCUSSION.getType());
        return commentList;
    }

    public void deleteByParentId(Comment comment) {
        commentMapper.deleteByParentId(comment);
    }
}
