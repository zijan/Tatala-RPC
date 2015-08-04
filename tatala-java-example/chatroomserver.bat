@echo off
set classpath=.
set classpath=%classpath%;lib\*
set classpath=%classpath%;target\*

java com.qileyuan.tatala.example.server.ChatRoomServer 10002
