package com.dayee.exception;


public class FileSizeException extends CustomizeException {

    private static final long serialVersionUID = 1L;

    public FileSizeException(String code,Object message){
        super(code,message.toString());
        this.code = code;
    }
}
