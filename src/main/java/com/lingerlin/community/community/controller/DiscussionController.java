package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lingerlin
 */
@Controller
public class DiscussionController {
    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/discussion/{id}")
    public String discussion(
            Model model,
            @PathVariable(name = "id") Integer id){
        DiscussionDTO discussionDTO = discussionService.getById(id);
        model.addAttribute("discussion",discussionDTO);
        return "discussion";
    }
}
