package com.dayee.exception;

public class PhoneInterfaceException extends CustomizeException {

	private static final long serialVersionUID = 1L;

	public PhoneInterfaceException(Object message){
		super(message.toString());
	}
}
