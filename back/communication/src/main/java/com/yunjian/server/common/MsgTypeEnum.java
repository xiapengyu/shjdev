package com.yunjian.server.common;

import org.apache.commons.lang3.StringUtils;

public enum MsgTypeEnum {
	HEARTBEAT("HEARTBEAT","心跳", (short)4097), //
	UPLOAD_DEVICE_CODE("UPLOAD_DEVICE_CODE","上报机器编号", (short)8193), //
	UPLOAD_DEVICE_STATUS("UPLOAD_DEVICE_STATUS","上报机器状态", (short)12289), //
	UPLOAD_REBOOT_RESULT("UPLOAD_REBOOT_RESULT","上报远程重启机器结果", (short)12290), //
	UPLOAD_TEMP_CONFIG_RESULT("UPLOAD_TEMP_CONFIG_RESULT","上报远程设置柜子温度结果", (short)16385), //
	UPLOAD_LIGHT_CONFIG_RESULT("UPLOAD_LIGHT_CONFIG_RESULT","上报远程设置柜子灯光结果", (short)16386), //
	UPLOAD_GOODS_OUT_RESULT("UPLOAD_GOODS_OUT_RESULT","商品出货结果", (short)24577), //
	UPLOAD_DEVICE_PRIMARY_PASSWORD("UPLOAD_DEVICE_PRIMARY_PASSWORD","上报管理页密码", (short)16387), //
	UPLOAD_DEVICE_SECOND_PASSWORD("UPLOAD_DEVICE_SECOND_PASSWORD","上报测试页密码", (short)16389), //
	;
	
	// 命令
	String type;
	// 名称
	String name;
	// 编号
	short no;
	
	/**
	 * 构造方法.
	 */
	private MsgTypeEnum(String type, String name, short no) {
		this.type = type;
		this.name = name;
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public short getNo() {
		return no;
	}

	@Override
	public String toString() {
		return "EventTypeEnum [type=" + name + ", name=" + name + ", no=" + no + "]";
	}

	/**
	 * 通过名称拿到编号 String.
	 * 
	 * @param name required
	 * @return
	 */
	public static short getNoByName(String name) {
		// 如果对象为空直接返回
		if (StringUtils.isEmpty(name)) {
			return 0;
		}
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			// 当传入名称和枚举的名称相等
			if (StringUtils.trim(name).equals(temp.getName())) {
				return temp.getNo();
			}
		}
		return 0;
	}

	/**
	 * 通过编号拿到名称 String.
	 */
	public static String getNameByNo(short no) {
		// 如果对象为空直接返回
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			// 当传入号码和枚举的号码相等
			if (temp.getNo() == no) {
				return temp.getName();
			}
		}
		return null;
	}

	/**
	 * 通过编号拿到命令 String.
	 * 
	 * @param no required
	 * @return
	 */
	public static String getCommandByNo(byte no) {
		if (no == 0) {
			return null;
		}
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			if (temp.getNo()==no) {
				return temp.getType();
			}
		}
		return null;
	}

	/**
	 * 通过命令拿到编号 String.
	 * 
	 * @param command required
	 * @return
	 */
	public static short getNoByType(String command) {
		// 如果对象为空直接返回
		if (StringUtils.isEmpty(command)) {
			return 00;
		}
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (StringUtils.trim(command).equals(temp.getType())) {
				return temp.getNo();
			}
		}
		return 00;
	}

	/**
	 * 通过命令拿到枚举 String.
	 * 
	 * @param command required
	 * @return
	 */
	public static MsgTypeEnum getEnumByCommand(String command) {
		// 如果传入的命令为空，则直接返回null
		if (StringUtils.isEmpty(command)) {
			return null;
		}
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getType().equals(command.trim())) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * 通过编号拿到枚举 String.
	 * 
	 * @param no required
	 * @return
	 */
	public static MsgTypeEnum getEnumByNo(byte no) {
		// 如果对象为空直接返回
		if (no == (byte)0x00) {
			return null;
		}
		MsgTypeEnum[] types = MsgTypeEnum.values();
		// 遍历枚举
		for (MsgTypeEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getNo()==no) {
				return temp;
			}
		}
		return null;
	}
}
