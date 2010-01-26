package com.thoughtworks.moneydroid.sms.handlers;

public class InvalidSmsFormatException extends RuntimeException {

	public InvalidSmsFormatException(String message) {
		super(message);
	}

}
