package com.qileyuan.tatala.socket.to;

import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;

/**
 * This is customization object wrapper interface.
 * 
 * @author JimT
 *
 */
public interface TransferObjectWrapper {
	public int getLength();
	public void getByteArray(TransferOutputStream touts);
	public TransferObjectWrapper getObjectWrapper(TransferInputStream tins);
}
