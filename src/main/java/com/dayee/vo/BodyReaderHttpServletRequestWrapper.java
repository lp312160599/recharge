package com.dayee.vo;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper{

    private byte dataByte[];

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }
    
    @Override
    public ServletInputStream getInputStream() throws IOException {
        
        if(dataByte==null){
            ServletInputStream  input = super.getInputStream();
            dataByte = new byte[getContentLength()];
            input.read(dataByte);
            input.close();
        }
        
        final ByteArrayInputStream bais = new ByteArrayInputStream(dataByte);
        return new ServletInputStream() {
            public int read() throws IOException {
                return bais.read();
            }
        };
    
    }
    
}
