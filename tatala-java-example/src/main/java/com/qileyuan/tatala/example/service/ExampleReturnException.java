package com.qileyuan.tatala.example.service;

import com.qileyuan.tatala.socket.exception.TatalaRollbackException;


public class ExampleReturnException extends TatalaRollbackException{
	private static final long serialVersionUID = 1L;

	public ExampleReturnException() {
		super();
	}

	public ExampleReturnException(String msg) {
		super(msg);
	}
}
