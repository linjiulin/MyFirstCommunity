package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.service.DiscussionService;
import com.lingerlin.community.community.service.DiscussionServiceMongo;
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
    private DiscussionService discussionService;

    @Autowired
    private DiscussionServiceMongo discussionServiceMongo;

    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size,
                        @RequestParam(name = "search",required = false) String search,
                         Model model) {
        //使用MySQL
//        PageDTO pageDTO = discussionService.list(search,page, size);
//        model.addAttribute("pages", pageDTO);

        //使用MongoDB
        PageDTO pageDTOMongo = discussionServiceMongo.listAll(search,page, size);
        model.addAttribute("pages", pageDTOMongo);
        model.addAttribute("search", search);
        return "index";
    }
}
