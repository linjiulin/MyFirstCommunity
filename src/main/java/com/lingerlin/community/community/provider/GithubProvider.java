package com.lingerlin.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.lingerlin.community.community.dto.AccessTokenDto;
import com.lingerlin.community.community.dto.GithubUser;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
/*
泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
当放了Component这个注解，对象就自动实例化放在一个组件池中，当使用时可以轻松提取使用
 */
public class GithubProvider {
    /*
    github交换code时需要传递五个参数
    client_id	string	Required. The client ID you received from GitHub for your GitHub App.
    client_secret	string	Required. The client secret you received from GitHub for your GitHub App.
    code	string	Required. The code you received as a response to Step 1.
    redirect_uri	string	The URL in your application where users are sent after authorization.
    state	string	The unguessable random string you provided in Step 1.
    由于参数数量较多，应该将参数封装成一个类(存放在dto(Data Transfer Object数据传输对象)包中)
     */
    public String getAccessToken(AccessTokenDto accessTokenDto) {

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =  response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getUser(String accessToken) throws Exception{
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.github.com/user?access_token="+accessToken)
//                .header("Authorization","token "+accessToken)
//                .build();
//        Response response = null;
//        try {
//            response = client.newCall(request).execute();
//            String string = response.body().string();
//            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
//            return githubUser;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.github.com/user");
        httpget.addHeader(new BasicHeader("Authorization", "token "+accessToken));
        httpget.setProtocolVersion(HttpVersion.HTTP_1_0);
//        httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpget.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            GithubUser user = JSON.parseObject(entity.getContent(),GithubUser.class);
            EntityUtils.consume(entity);
            response.close();
            return  user;
        } catch (Exception e){
            throw e;
        } finally{
            if(response != null){
                response.close();
            }
            if(httpclient!=null){
                httpclient.close();
            }
        }
    }
}
