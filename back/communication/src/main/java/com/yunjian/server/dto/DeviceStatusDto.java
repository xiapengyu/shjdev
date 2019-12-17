package com.yunjian.server.dto;

import lombok.Data;

@Data
public class DeviceStatusDto {
	
	private short lockerNo;                  //2byte,柜子号
	private short temp;                      //2byte,柜子温度
	private short lamp;                      //2byte,柜子灯光
	private short defogStatus;               //2byte,除雾状态
	private short doorStatus;                //2byte，柜门状态
	private short faultCode;                 //2byte,最近故障代码
	private short faultTime;                 //2byte,最近故障时间
	private short tempCtrlStatus;            //2byte,温度控制状态
	private short version;                   //2byte,下位机版本
	private short settedTemp;                //2byte,设定的温度
	private byte signal;                     //1byte,信号
	private short appVersion;                //2byte,app版本号
	private String deviceCode;               //50byte,设备编号

}
