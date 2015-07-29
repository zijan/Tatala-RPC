package com.qileyuan.tatala.socket.exception;

public class TatalaRollbackException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TatalaRollbackException() {
		super();
	}

	public TatalaRollbackException(String msg) {
		super(msg);
	}
}
