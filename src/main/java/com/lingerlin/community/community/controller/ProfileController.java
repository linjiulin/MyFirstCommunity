package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.model.UserMongo;
import com.lingerlin.community.community.service.DiscussionService;
import com.lingerlin.community.community.service.DiscussionServiceMongo;
import com.lingerlin.community.community.service.NotificationService;
import com.lingerlin.community.community.service.NotificationServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private DiscussionServiceMongo discussionServiceMongo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationServiceMongo notificationServiceMongo;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable(name = "action")String action,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            Model model){

//        User user = (User)request.getSession().getAttribute("user");
        UserMongo user = (UserMongo) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if("discussions".equals(action)){
            model.addAttribute("section","discussions");
            model.addAttribute("sectionName","我的讨论");
            //使用MysSQL
//            PageDTO pageDTO = discussionService.list(user.getId(),page,size);

            //使用MongoDB
            PageDTO pageDTO = discussionServiceMongo.list(user.get_id(),page,size);
            model.addAttribute("pages", pageDTO);
        }
        else if("replies".equals(action)){
            //使用MySQL
//            PageDTO pageDTO = notificationService.list(user.getId(),page,size);
//            Integer unreadCount = notificationService.unreadCount(user.getId());

            //使用MongoDB
            PageDTO pageDTO = notificationServiceMongo.list(user.get_id(),page,size);
            Integer unreadCount = notificationServiceMongo.unreadCount(user.get_id());

            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("pages", pageDTO);
        }
            return "profile";
    }
}
