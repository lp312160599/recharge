package com.dayee.exception;


public class DayeeVideoConsumptionLogException extends CustomizeException {

    private static final long serialVersionUID = 1L;
    
    public DayeeVideoConsumptionLogException(String code,Object message){
        super(code,message.toString());
        this.code = code;
    }
}
