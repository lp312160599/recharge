package com.dayee.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.dayee.utils.NumberUtil;


public class FilSizeConverterTag  extends TagSupport{

    private static final long serialVersionUID = 1L;
    
    private int fileSize;
    
    @Override
    public int doStartTag() throws JspException {
        
        try {
            String text = String.valueOf(NumberUtil.division((double)fileSize, 1024*1024));
            JspWriter writer=this.pageContext.getOut();
            writer.write(text);
        } catch (IOException e) {
        }
        return BodyTagSupport.EVAL_PAGE;
    }
    
    public int getFileSize() {
    
        return fileSize;
    }
    
    public void setFileSize(int fileSize) {
    
        this.fileSize = fileSize;
    }
}
