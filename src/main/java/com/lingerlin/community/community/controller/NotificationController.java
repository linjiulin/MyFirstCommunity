package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.NotificationDTO;
import com.lingerlin.community.community.dto.NotificationMongoDTO;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.model.UserMongo;
import com.lingerlin.community.community.service.NotificationService;
import com.lingerlin.community.community.service.NotificationServiceMongo;
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

    @Autowired
    NotificationServiceMongo notificationServiceMongo;


    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest httpServletRequest,
//                          @PathVariable(name = "id") Integer id){
                          @PathVariable(name = "id") String id){

//        User user = (User)httpServletRequest.getSession().getAttribute("user");
        UserMongo user = (UserMongo)httpServletRequest.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        //使用MySQL
//        NotificationDTO notificationDTO = notificationService.read(id,user);
//        return "redirect:/discussion/" + notificationDTO.getOuterId();

        //使用MongoDB
        NotificationMongoDTO notificationDTO = notificationServiceMongo.read(id,user);
        return "redirect:/discussion/" + notificationDTO.getOuterId();
    }
}
