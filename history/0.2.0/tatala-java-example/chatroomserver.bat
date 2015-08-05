@echo off
set classpath=.
set classpath=%classpath%;lib\*
set classpath=%classpath%;dist\tatala-java-example.jar
set classpath=%classpath%;cfg

java com.qileyuan.tatala.example.server.ChatRoomServer 10002
