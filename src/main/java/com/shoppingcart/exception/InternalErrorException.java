package com.shoppingcart.exception;

public class InternalErrorException extends RuntimeException{
	private static final long serialVersionUID = 4806296251749052484L;

	public InternalErrorException() {
		super("Internal Error Occured");
	}
}
