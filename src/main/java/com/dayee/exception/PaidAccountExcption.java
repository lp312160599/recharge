package com.dayee.exception;


public class PaidAccountExcption extends CustomizeException {

    private static final long serialVersionUID = 1L;

    public PaidAccountExcption(Object message){
        super(message.toString());
    }

}
