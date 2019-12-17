package com.yunjian.server.common;

public class Constant {
	
	/**
	 * 机器编码上报的响应内容
	 */
	public static byte[] HEARTBEAT_RESP = {(byte)0x02,(byte)0x10,(byte)0x00,(byte)0x07,(byte)0x01,(byte)0x0D,(byte)0x0A};
	
	/**
	 * 机器编码上报的响应内容
	 */
	public static byte[] DEVICE_CODE_RESP = {(byte)0x02,(byte)0x20,(byte)0x00,(byte)0x07,(byte)0x01,(byte)0x0d,(byte)0x0a};
	
	//============ 下行控制指令 ========================
	
	/**
	 * 远程重启设备
	 */
	public static byte[] REBOOT_DEVICE = {(byte)0x02,(byte)0x30,(byte)0x00,(byte)0x07,(byte)0x02,(byte)0x0d,(byte)0x0a};
	
	/**
	 * 远程设置密码
	 */
	public static byte[] CONFIG_PASSWORD = {(byte)0x02,(byte)0x40,(byte)0x00,(byte)0x07,(byte)0x03,(byte)0x0d,(byte)0x0a};
	
	/**
	 * 心跳检测周期数
	 */
	public static final int HEARTBEAT_SYCLE_TIME = 2; 
	
	
}
