package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        Model model) {

        PageDTO pageDTO = discussionService.list(page, size);
        model.addAttribute("pages", pageDTO);
        return "index";
    }
}
