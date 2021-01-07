package com.lingerlin.community.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

/**
 * @author lingerlin
 */
@SpringBootApplication
public class CommunityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    /**
     * @param servletContext servlet上下文
     * @throws ServletException Servlet异常
     * 重写方法用于删除URL中的JSESSIONID参数
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
    }
}
