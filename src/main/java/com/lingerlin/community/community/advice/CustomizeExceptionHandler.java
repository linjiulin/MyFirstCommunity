package com.lingerlin.community.community.advice;

import com.lingerlin.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lingerlin
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest httpServletRequest,
                        Throwable throwable,
                        Model model){
        HttpStatus httpStatus = getStatus(httpServletRequest);
        if(throwable instanceof CustomizeException){
            model.addAttribute("message",throwable.getMessage());
        }
        else{
            model.addAttribute("message","未知错误");
        }

        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest httpServletRequest) {
        Integer statusCode = (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
