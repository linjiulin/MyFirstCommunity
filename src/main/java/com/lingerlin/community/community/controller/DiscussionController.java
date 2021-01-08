package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.CommentDTO;
import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.enums.CommentTypeEnum;
import com.lingerlin.community.community.service.CommentService;
import com.lingerlin.community.community.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author lingerlin
 */
@Controller
public class DiscussionController {
    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/discussion/{id}")
    public String discussion(
            Model model,
            @PathVariable(name = "id") Integer id){
        DiscussionDTO discussionDTO = discussionService.getById(id);
        List<CommentDTO> commentDTOList = commentService.listByParentId(id, CommentTypeEnum.DISCUSSION.getType());
        //累加阅读数
        discussionService.incView(id);
        model.addAttribute("discussion",discussionDTO);
        model.addAttribute("comment",commentDTOList);
        return "discussion";
    }
}
