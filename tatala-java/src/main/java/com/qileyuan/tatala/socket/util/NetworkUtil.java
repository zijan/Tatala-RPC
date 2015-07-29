package com.qileyuan.tatala.socket.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class NetworkUtil {
    public static long convertIpPortToUniqueId(byte[] quad, int port) {
        return (((long)port) << 32
                    | ((long)(quad[0] & 0xFF)) << 24
                    | ((long)(quad[1] & 0xFF)) << 16
                    | ((long)(quad[2] & 0xFF)) << 8
                    | ((long)(quad[3] & 0xFF)));
    }
    
    public static long getClientIdBySocketChannel(AsynchronousSocketChannel socketChannel) throws IOException{
    	InetSocketAddress address = (InetSocketAddress)socketChannel.getRemoteAddress();
    	byte[] quad = address.getAddress().getAddress();
		int port = address.getPort();
		long clientId = NetworkUtil.convertIpPortToUniqueId(quad, port);
		return clientId;
    }
    
    public static String getIpPortByClientId(long clientId){
    	long port = clientId >> 32;
        long ip1 = (clientId >> 24) - (port << 8);
        long ip2 = (clientId >> 16) - (ip1 << 8) - (port << 16);
        long ip3 = (clientId >> 8) - (ip2 << 8) - (ip1 << 16) - (port << 24);
        long ip4 = clientId - (ip3 << 8) - (ip2 << 16) - (ip1 << 24) - (port << 32);
        return ip1+"."+ip2+"."+ip3+"."+ip4+":"+port;
    }
}
