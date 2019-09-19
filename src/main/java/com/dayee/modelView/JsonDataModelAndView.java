package com.dayee.modelView;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.alibaba.fastjson.JSONArray;

public class JsonDataModelAndView extends ModelAndView {

    
    private Object objectJson;
   
    public JsonDataModelAndView(Object objectJson){
        super();
        this.objectJson= objectJson;
    }
    
    @Override
    public View getView() {

        return new View() {

            @Override
            public String getContentType() {

                 return "application/json;charset=utf-8";
            }

            @Override
            public void render(Map<String, ?> arg0, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {

                response.setContentType(getContentType());
                PrintWriter out =   response.getWriter();
                if(objectJson!=null) {
                   String json  =  JSONArray.toJSONString(objectJson);
                   out.write(json);
                   out.flush();
                   out.close();
                }
            }
        };
    }
}
