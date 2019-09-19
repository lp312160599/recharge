package com.dayee.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.dayee.utils.NumberUtil;


public class SecondConverterTag  extends TagSupport{

    private static final long serialVersionUID = 1L;
    
    private int second;
    
    @Override
    public int doStartTag() throws JspException {
        
        String text = null;
        if(second<60){
            text = second+"秒";
        }else{
            int s = second%60;
            int m = (int) NumberUtil.division(second, 60) ;
            text = m+"分钟";
            if(s!=0){
                text+=s+"秒";   
            }
        }
        try {
            JspWriter writer=this.pageContext.getOut();
            writer.write(text);
        } catch (IOException e) {
        }
        return BodyTagSupport.EVAL_PAGE;
    }

    public int getSecond() {
    
        return second;
    }
    
    public void setSecond(int second) {
    
        this.second = second;
    }
}
