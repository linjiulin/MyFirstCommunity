package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.*;
import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.model.Comment;
import com.lingerlin.community.community.model.CommentMongo;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.model.UserMongo;
import com.lingerlin.community.community.service.CommentService;
import com.lingerlin.community.community.service.CommentServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lingerlin
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentServiceMongo commentServiceMongo;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
//    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
    public Object post(@RequestBody CommentCreateMongoDTO commentCreateMongoDTO,
                       HttpServletRequest request){
//        User user = (User) request.getSession().getAttribute("user");
        UserMongo user = (UserMongo) request.getSession().getAttribute("user");

        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

//        //以下为使用MySQL存储
//        Comment comment = new Comment();
//        comment.setParentId(commentCreateDTO.getParentId());
//        comment.setContent(commentCreateDTO.getContent());
//        comment.setType(commentCreateDTO.getType());
//        comment.setGmtCreate(System.currentTimeMillis());
//        comment.setGmtModified(comment.getGmtCreate());
//        comment.setCommentator(user.getId());
//        comment.setLikeCount(0L);
//        commentService.insert(comment,user);

        CommentMongo commentMongo = new CommentMongo();
        commentMongo.setCommentCount(0);
        commentMongo.setParentId(commentCreateMongoDTO.getParentId());
        commentMongo.setContent(commentCreateMongoDTO.getContent());
        commentMongo.setType(commentCreateMongoDTO.getType());
        commentMongo.setGmtCreate(System.currentTimeMillis());
        commentMongo.setGmtModified(commentMongo.getGmtCreate());
        commentMongo.setCommentator(user.get_id());
        commentMongo.setLikeCount(0L);
        commentServiceMongo.insert(commentMongo,user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
//    public ResultDTO<List> comment(@PathVariable(name="id") Integer id){
    public ResultDTO<List> comment(@PathVariable(name="id") String id){
//        List<CommentDTO> commentDTOList = commentService.listByParentId(id, CommentTypeEnum.COMMENT.getType());
        List<CommentMongoDTO> commentMongoDTOList = commentServiceMongo.listByParentId(id, CommentTypeEnum.COMMENT.getType());
//        return ResultDTO.okOf(commentDTOList);
        return ResultDTO.okOf(commentMongoDTOList);
    }
}
