package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.service.DiscussionService;
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
    private UserMapper userMapper;

    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable(name = "action")String action,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            Model model){
        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null  && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if(user==null){
            return "redirect:/";
        }
        if("discussions".equals(action)){
            model.addAttribute("section","discussions");
            model.addAttribute("sectionName","我的讨论");
        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
