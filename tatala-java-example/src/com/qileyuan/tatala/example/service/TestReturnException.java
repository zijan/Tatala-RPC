package com.qileyuan.tatala.example.service;

import com.qileyuan.tatala.socket.TatalaReturnException;

public class TestReturnException extends TatalaReturnException{
	private static final long serialVersionUID = 1L;

	public TestReturnException() {
		super();
	}

	public TestReturnException(String msg) {
		super(msg);
	}
}
