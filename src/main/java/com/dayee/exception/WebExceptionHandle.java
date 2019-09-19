package com.dayee.exception;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import com.dayee.modelView.JsonDataModelAndView;

@ControllerAdvice
public class WebExceptionHandle {
    private static Logger logger = Logger.getLogger(WebExceptionHandle.class);

    /**
     * 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView  handleException(Exception e,HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        String uri = request.getRequestURI();
        
        String code=null;
        String msg=null;
        
        if(e instanceof CustomizeException) {
            code = ((CustomizeException) e).getCode();
            msg  = e.getMessage(); 
        }else {
            code="-1";
            msg = "系统发生异常,请联系管理员!";
        }
        String requestedWith = request.getHeader("X-Requested-With");
        if(uri.contains("/api/")
          ||"XMLHttpRequest".equals(requestedWith)) {
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("error", code);
            map.put("msg", msg);
            return new JsonDataModelAndView(map);
        }else {
            ModelAndView mv = new ModelAndView();  
            mv.addObject("message",msg);  
            mv.setViewName("tip/error"); 
            return mv;
        }
    }
}