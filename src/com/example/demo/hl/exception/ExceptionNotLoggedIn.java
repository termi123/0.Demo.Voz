package com.example.demo.hl.exception;

public class ExceptionNotLoggedIn extends Exception{

	private static final long serialVersionUID = 1L;

	public ExceptionNotLoggedIn(){
		super("Your are not logged in.");
	}
}
