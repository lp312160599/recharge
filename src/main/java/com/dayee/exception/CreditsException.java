package com.dayee.exception;


public class CreditsException extends CustomizeException {

    private static final long serialVersionUID = 1L;

    public CreditsException(String code,Object message){
        super(code,message.toString());
    }
}
