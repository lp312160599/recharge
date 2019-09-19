package com.dayee.exception;


public abstract class CustomizeException extends Exception {

    private static final long serialVersionUID = 1L;
    
    protected String code = null;

    public CustomizeException(String code,Object message) {
        super(message.toString());
        this.code = code;
    }
    
    public CustomizeException(Object message) {
        super(message.toString());
        this.code = "-1";
    }
    
    public String getCode() {
        
        return code;
    }
    
    public void setCode(String code) {
    
        this.code = code;
    }
}
