package com.lingerlin.community.community.interceptor;

import com.lingerlin.community.community.dao.MongoUserDao;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Notification;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.model.UserMongo;
import com.lingerlin.community.community.service.NotificationService;
import com.lingerlin.community.community.service.NotificationServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MongoUserDao mongoUserDao;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationServiceMongo notificationServiceMongo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length!=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);

                    UserMongo userMongo = mongoUserDao.findByToken(token);
//                    if (user != null) {
//                        Integer unreadCount = notificationService.unreadCount(user.getId());
//                        request.getSession().setAttribute("user", user);
//                        request.getSession().setAttribute("unreadCount",unreadCount);
//                    }

                    if (userMongo != null) {
                        Integer unreadCount = notificationServiceMongo.unreadCount(userMongo.get_id());
                        request.getSession().setAttribute("user", userMongo);
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
