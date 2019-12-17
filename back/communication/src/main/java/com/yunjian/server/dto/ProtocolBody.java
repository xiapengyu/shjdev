package com.yunjian.server.dto;

import lombok.Data;

@Data
public class ProtocolBody {
	
	private byte cardCode = (byte)0x02;  //1 字节，起始码 

	private byte type;                    //1 字节，贞类型
	
	private short length;                 //2字节，贞长度
	
	private byte ctrlCode;                //1 字节，功能码,
	
	private byte[] data;                  //数据域
	
	private short end;                    //结束码 {0x0d,0x0a}
	
	private short msgType;                //扩展字段，用于标识消息类型，不属于通信协议内字段
	
	private String deviceCode;            //扩展字段，设备编码，不属于通信协议内字段
	
}
