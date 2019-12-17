package com.yunjian.api.constant;

import org.apache.commons.lang3.StringUtils;

public enum CommandEnum {
	//下行控制字
	MECHINE_CTRL("MECHINE_CTRL", "电机控制指令", (byte)0x30 ),  //
	MECHINE_CTRL_INFRARED_DETECTION("MECHINE_CTRL_INFRARED_DETECTION", "电机控制+红外检测指令", (byte)0x31 ),  //
	QUERY_STATUS("QUERY_STATUS", "状态查询指令", (byte)0x32 ),  //
	UPLOAD_EVENT("UPLOAD_EVENT", "按键/仓门事件上报", (byte)0xB3 ),  //
	POWER_OUTPUT_CTRL("POWER_OUTPUT_CTRL", "电源输出控制", (byte)0x34 ),  //
	SET_ADDRESS("SET_ADDRESS", "地址设置指令", (byte)0x35 ),  //
	READ_VERSION("READ_VERSION", "读版本指令", (byte)0x36 ),  //
	TEST_COMMAND("TEST_COMMAND", "测试指令", (byte)0x37 ),  //
	SOFTWARE_RESET("SOFTWARE_RESET", "软件复位指令", (byte)0x38 ),  //
	UPLOAD_START("UPLOAD_START", "开机上报指令", (byte)0xB8 ),  //
	CAGE_ASSEMBLY_CTRL("CAGE_ASSEMBLY_CTRL", "升降台控制指令", (byte)0x39 ),  //
	SECURITY_DOOR_CTRL("SECURITY_DOOR_CTRL", "防盗门控制指令", (byte)0x3A ),  //
	CABIN_DOOR_LOCK("CABIN_DOOR_LOCK", "仓门锁控制指令", (byte)0x3B ),  //
	CANCEL_CURRENT_OPT("CANCEL_CURRENT_OPT", "取消当前操作指令", (byte)0x3F ),  //
	READ_TEMP("READ_TEMP", "读取温度", (byte)0x40 ),  //
	SOFTWARE_UPGRADE("SOFTWARE_UPGRADE", "软件升级", (byte)0x7F ),  //
	
	//上行控制字
	MECHINE_CTRL_RESULT("MECHINE_CTRL_RESULT", "响应电机控制指令", (byte)0xb0 ),
	MECHINE_CTRL_INFRARED_DETECTION_RESULT("MECHINE_CTRL_INFRARED_DETECTION_RESULT", "响应电机控制+红外检测指令", (byte)0xb1 ),  //
	QUERY_STATUS_RESULT("QUERY_STATUS_RESULT", "响应状态查询指令", (byte)0xb2 ),  //
	UPLOAD_EVENT_RESULT("UPLOAD_EVENT_RESULT", "响应按键/仓门事件上报", (byte)0xb3 ),  //
	//POWER_OUTPUT_CTRL_RESULT("POWER_OUTPUT_CTRL_RESULT", "响应电源输出控制", (byte)0xb5 ),  //
	SET_ADDRESS_RESULT("SET_ADDRESS_RESULT", "响应地址设置指令", (byte)0xb5 ),  //
	READ_VERSION_RESULT("READ_VERSION_RESULT", "读版本指令", (byte)0xb6 ),  //
//	TEST_COMMAND("TEST_COMMAND", "测试指令", (byte)0x37 ),  //
	SOFTWARE_RESET_RESULT("SOFTWARE_RESET_RESULT", "软件复位指令", (byte)0xb8 ),  //
	UPLOAD_START_RESULT("UPLOAD_START_RESULT", "开机上报指令", (byte)0xb8 ),  //
	CAGE_ASSEMBLY_CTRL_RESULT("CAGE_ASSEMBLY_CTRL_RESULT", "升降台控制指令", (byte)0xb9 ),  //
	SECURITY_DOOR_CTRL_RESULT("SECURITY_DOOR_CTRL_RESULT", "防盗门控制指令", (byte)0xbA ),  //
	CABIN_DOOR_LOCK_RESULT("CABIN_DOOR_LOCK_RESULT", "仓门锁控制指令", (byte)0xbB ),  //
	CANCEL_CURRENT_OPT_RESULT("CANCEL_CURRENT_OPT_RESULT", "取消当前操作指令", (byte)0xbF ),  //
	READ_TEMP_RESULT("READ_TEMP_RESULT", "读取温度", (byte)0xC0 ),  //
	SOFTWARE_UPGRADE_RESULT("SOFTWARE_UPGRADE_RESULT", "软件升级", (byte)0xFF )  //
	;
	
	// 命令
	String command;
	// 名称
	String name;
	// 编号
	byte no;
	
	/**
	 * 构造方法.
	 * 
	 * @param command required
	 * @param name    required
	 * @param no      required
	 */
	private CommandEnum(String command, String name, byte no) {
		this.command = command;
		this.name = name;
		this.no = no;
	}

	public String getCommand() {
		return command;
	}

	public String getName() {
		return name;
	}

	public byte getNo() {
		return no;
	}

	@Override
	public String toString() {
		return "EventTypeEnum [command=" + command + ", name=" + name + ", no=" + no + "]";
	}

	/**
	 * 通过名称拿到编号 String.
	 * 
	 * @param name required
	 * @return
	 */
	public static byte getNoByName(String name) {
		// 如果对象为空直接返回
		if (StringUtils.isEmpty(name)) {
			return 0;
		}
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
			// 当传入名称和枚举的名称相等
			if (StringUtils.trim(name).equals(temp.getName())) {
				return temp.getNo();
			}
		}
		return 0;
	}

	/**
	 * 通过编号拿到名称 String.
	 * 
	 * @param no required
	 * @return
	 */
	public static String getNameByNo(byte no) {
		// 如果对象为空直接返回
		if (no == 0) {
			return null;
		}
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
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
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
			if (temp.getNo()==no) {
				return temp.getCommand();
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
	public static byte getNoByCommand(String command) {
		// 如果对象为空直接返回
		if (StringUtils.isEmpty(command)) {
			return 00;
		}
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (StringUtils.trim(command).equals(temp.getCommand())) {
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
	public static CommandEnum getEnumByCommand(String command) {
		// 如果传入的命令为空，则直接返回null
		if (StringUtils.isEmpty(command)) {
			return null;
		}
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getCommand().equals(command.trim())) {
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
	public static CommandEnum getEnumByNo(byte no) {
		// 如果对象为空直接返回
		if (no == (byte)0x00) {
			return null;
		}
		CommandEnum[] types = CommandEnum.values();
		// 遍历枚举
		for (CommandEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getNo()==no) {
				return temp;
			}
		}
		return null;
	}
}
