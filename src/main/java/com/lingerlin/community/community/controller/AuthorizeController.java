package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.dto.AccessTokenDto;
import com.lingerlin.community.community.dto.GithubUser;
import com.lingerlin.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDto accessTokenDto = new AccessTokenDto(); //ctrl+shift+v 能快速实例化一个对象
        //shift+enter能快速换行并让光标移到最前
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        System.out.println(user.getId());
        return "index";

    }

}
