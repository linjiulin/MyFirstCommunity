package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.AccessTokenDto;
import com.lingerlin.community.community.dto.GithubUser;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.model.UserMongo;
import com.lingerlin.community.community.provider.GithubProvider;
import com.lingerlin.community.community.service.UserService;
import com.lingerlin.community.community.service.UserServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired//自动将Spring容器中些写好的事例加载到上下文
    private GithubProvider githubProvider;

    @Value("${github.client_id}")
    private String clientId;

    /**这个注解会自动去配置文件中读取Key是client_id的value*/
    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;


    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceMongo userServiceMongo;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,//session是从request中拿到，当将HttpServletRequest放在方法中，Spring就会自动从上下文Request
                           HttpServletResponse response) throws Exception {
        AccessTokenDto accessTokenDto = new AccessTokenDto(); //ctrl+shift+v 能快速实例化一个对象
        //shift+enter能快速换行并让光标移到最前
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        System.out.println("AT是："+accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser !=null){
            //登录成功，写cookie和session

             //用MySQL来判断
//            User user = new User();
//            //抽出token的目的是为了让其代替cookie
//            String token = UUID.randomUUID().toString();
//            //判断用户
//            userService.checkUser(githubUser,response,token,user);

            //用MongoDB来判断
            UserMongo user = new UserMongo();
            //抽出token的目的是为了让其代替cookie
            String token = UUID.randomUUID().toString();
            //判断用户
            userServiceMongo.checkUserByMongo(githubUser,response,token,user);


            return "redirect:/";//重定向到根目录页面
        }
        else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    /**
     * @param session
     * @param response
     * @return
     */
    /*退出登录 */
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         HttpServletResponse response){
        session.removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
