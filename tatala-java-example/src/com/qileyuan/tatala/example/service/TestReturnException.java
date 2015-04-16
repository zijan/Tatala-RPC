package com.qileyuan.tatala.example.service;

import com.qileyuan.tatala.socket.exception.TatalaRollbackException;


public class TestReturnException extends TatalaRollbackException{
	private static final long serialVersionUID = 1L;

	public TestReturnException() {
		super();
	}

	public TestReturnException(String msg) {
		super(msg);
	}
}
