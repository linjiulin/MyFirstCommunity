package com.lingerlin.community.community.advice;

import com.alibaba.fastjson.JSON;
import com.lingerlin.community.community.dto.ResultDTO;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lingerlin
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest httpServletRequest,
                  Throwable throwable,
                  Model model,
                  HttpServletResponse httpServletResponse){
        String contentType = httpServletRequest.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            //返回JSON
            if(throwable instanceof CustomizeException){
                resultDTO  = ResultDTO.errorOf((CustomizeException) throwable);
            }
            else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try{
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        else{
            //错误页面跳转
            if(throwable instanceof CustomizeException){
                model.addAttribute("message",throwable.getMessage());
            }
            else{
                model.addAttribute(CustomizeErrorCode.SYS_ERROR);
            }
            return new ModelAndView("error");
        }
    }
}
