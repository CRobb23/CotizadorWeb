package com.digitalgeko.servicebus.exceptions;

public class JSONMarshalException extends Exception {

	public JSONMarshalException(String message) {
		super(message);
	}

	public JSONMarshalException(Throwable cause) {
		super(cause);
	}

	public JSONMarshalException(String message, Throwable cause) {
		super(message, cause);
	}

}
