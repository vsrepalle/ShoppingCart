package com.shoppingcart.exception;

public class InvalidQuantityException extends RuntimeException{
	private static final long serialVersionUID = 5592655664993668247L;

	public InvalidQuantityException() {
		super("Sorry...! Quantity can't be negative");
	}
}
