package com.lingerlin.community.community.service;

import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.mapper.CommentMapper;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.model.Comment;
import com.lingerlin.community.community.model.Discussion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussionMapper discussionMapper;
    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            //回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
        else{
            Discussion discussion = discussionMapper.findById(comment.getParentId());
            if(discussion == null){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            discussionMapper.UpdateCommentCountById(comment.getParentId());
            //回复讨论
        }
    }
}
