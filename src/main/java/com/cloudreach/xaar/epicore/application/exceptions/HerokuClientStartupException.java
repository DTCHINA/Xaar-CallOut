package com.cloudreach.xaar.epicore.application.exceptions;

/**
 * @author alirezafallahi
 */
public class HerokuClientStartupException extends RuntimeException {
	
	public HerokuClientStartupException(Throwable t) {
		super(t);
	}
}
