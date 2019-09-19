package com.dayee.tag;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.dayee.utils.DateUtil;


public class TimeConverterTag  extends TagSupport{

    private static final long serialVersionUID = 1L;
    
    private long time;
    private String pattern;
    
    @Override
    public int doStartTag() throws JspException {
        
        try {
            Date d = new Date(time);
            String text = DateUtil.dateToString(d,pattern==null?DateUtil.YYYY_MM_DD_HH_MM_SS:pattern);
            JspWriter writer=this.pageContext.getOut();
            writer.write(text);
        } catch (IOException e) {
        }
        return BodyTagSupport.EVAL_PAGE;
    }

    
    public long getTime() {
    
        return time;
    }

    
    public void setTime(long time) {
    
        this.time = time;
    }

    public String getPattern() {
    
        return pattern;
    }
    
    public void setPattern(String pattern) {
    
        this.pattern = pattern;
    }
}
