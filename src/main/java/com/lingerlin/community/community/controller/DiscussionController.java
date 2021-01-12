package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.*;
import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.model.DiscussionMongo;
import com.lingerlin.community.community.service.CommentService;
import com.lingerlin.community.community.service.CommentServiceMongo;
import com.lingerlin.community.community.service.DiscussionService;
import com.lingerlin.community.community.service.DiscussionServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingerlin
 */
@Controller
public class DiscussionController {
    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private DiscussionServiceMongo discussionServiceMongo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentServiceMongo commentServiceMongo;

    @GetMapping("/discussion/{id}")
    public String discussion(
            Model model,
//            @PathVariable(name = "id") Integer id){
            @PathVariable(name = "id") String id){
//        //以下为使用MySQL
//        DiscussionDTO discussionDTO = discussionService.getById(id);
//        List<CommentDTO> commentDTOList = commentService.listByParentId(id, CommentTypeEnum.DISCUSSION.getType());
//        List<Discussion> relatedDiscussionList = discussionService.getRelatedDiscussionBytag(discussionDTO);

        //以下为使用MongoDB
        DiscussionMongoDTO discussionDTO = discussionServiceMongo.getById(id);
        model.addAttribute("discussion",discussionDTO);

        List<CommentMongoDTO> commentDTOList = commentServiceMongo.listByParentId(id, CommentTypeEnum.DISCUSSION.getType());
        model.addAttribute("comment",commentDTOList);

        List<DiscussionMongo> relatedDiscussionList = discussionServiceMongo.getRelatedDiscussionBytag(discussionDTO);
        model.addAttribute("realtedDiscussion",relatedDiscussionList);

        //累加阅读数.使用MySQL
//        discussionService.incView(id);

        //累加阅读数.使用MongoDB
        discussionServiceMongo.incView(id);

        return "discussion";
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
//    public Object doDelete(@RequestBody Integer id){
    public Object doDelete(@RequestBody String id){
        //使用MySQL
//        discussionService.deleteAllById(id);
        //使用Mongo
        discussionServiceMongo.deleteAllById(id);
        System.out.println(id);
        return ResultDTO.okOf();
    }
}
