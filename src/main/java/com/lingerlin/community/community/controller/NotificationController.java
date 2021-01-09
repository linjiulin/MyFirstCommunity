package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.NotificationDTO;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lingerlin
 */
@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest httpServletRequest,
                          @PathVariable(name = "id") Integer id){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        return "redirect:/discussion/" + notificationDTO.getOuterId();
    }
}
