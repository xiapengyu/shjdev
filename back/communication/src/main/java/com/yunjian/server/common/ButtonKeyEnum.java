package com.yunjian.server.common;

public enum ButtonKeyEnum {
	GLASS_DOOR_OPEN("玻璃门打开",(byte) 0x0A), // 
	GLASS_DOOR_CLOSE("玻璃门关闭",(byte) 0x0B), // 
	BIN_GATE_OPEN("仓门打开",(byte) 0x0C), // 
	BIN_GATECLOSE("仓门关闭",(byte) 0x0D), // 
	;

	private String name;
	private byte code;
	
	private ButtonKeyEnum(String name,byte code) {
		this.name = name;
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 通过编号拿到枚举 String.
	 * 
	 * @param no required
	 * @return
	 */
	public static ButtonKeyEnum getEnumByNo(Byte no) {
		// 如果对象为空直接返回
		if (no == null) {
			return null;
		}
		ButtonKeyEnum[] types = ButtonKeyEnum.values();
		// 遍历枚举
		for (ButtonKeyEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getCode()==no) {
				return temp;
			}
		}
		return null;
	}

}
