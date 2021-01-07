package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.GithubUser;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author lingerlin
 * 这个类是用来封装一些在需要时候才用到的方法
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    //此方法用来判断数据库中是否含有当前需要登录的用户
    public void checkUser(GithubUser githubUser, HttpServletResponse response,String token,User user) {
        if(userMapper.findByAccountId(String.valueOf(githubUser.getId()),githubUser.getName())!=null){
            userMapper.updateByAccountId(token,
                                        System.currentTimeMillis(),
                                        githubUser.getAvatar_url(),
                                        githubUser.getBio(),
                                        String.valueOf(githubUser.getId()));
            System.out.println("有这个用户");
            System.out.println(githubUser.getAvatar_url());
            response.addCookie(new Cookie("token",token));
        }
        else{
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setBio(githubUser.getBio());
            user.setAvatar(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
        }
    }
}
