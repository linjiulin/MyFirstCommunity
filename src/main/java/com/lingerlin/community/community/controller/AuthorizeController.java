package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.AccessTokenDto;
import com.lingerlin.community.community.dto.GithubUser;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired//自动将Spring容器中些写好的事例加载到上下文
    private GithubProvider githubProvider;

    @Value("${github.client_id}")
    private String clientId;

    //这个注解会自动去配置文件中读取Key是client_id的value
    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,//session是从request中拿到，当将HttpServletRequest放在方法中，Spring就会自动从上下文Request
                           HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto(); //ctrl+shift+v 能快速实例化一个对象
        //shift+enter能快速换行并让光标移到最前
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser.getName());
        System.out.println(githubUser.getId());
        if(githubUser !=null){
            //登录成功，写cookie和session
            User user = new User();

            String token = UUID.randomUUID().toString();//抽出token的目的是为了让其代替cookie
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
//            System.out.println("user.setName="+user.getName());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));

//            //登录成功，写入session和cookie
//            request.getSession().setAttribute("user",githubUser);//获取Session对象并将user对象放置在session中
            /*
            此时相当于在银行中已经创建成功一个银行账户，但是并未发给前端一个银行卡
            如果不手动给账户签发一个银行卡，就会自动生成一个默认的卡，无法自定义
             */
            return "redirect:/";//重定向到根目录页面
        }
        else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }

}
