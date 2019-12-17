package com.yunjian.api.constant;

public enum ExeStatusEnum {
	ERROR("错误",(byte) 0x00), //
	CORRECT("正常",(byte) 0x01), //
	GORGE_LINE_INCORRECT("串口错误", (byte) 0x02), //
	A_ROUND("电机转动一圈",(byte) 0x03), // 
	SHIPMENT_FAIL("出货失败",(byte) 0x04), // 
	SHIPMENT_FAIL2("出货失败",(byte) 0x05), // 
	TRADE_DROPED("红外检测到商品掉下 ",(byte) 0x06), // 
	A_ROUND_TRADE_DROPED("电机转动一圈，红外检测到商品掉下",(byte) 0x07), // 
	CAGE_ASSEMBLY_MOTOR_STUCK("升降台马达卡死",(byte) 0x08), // 
	CAGE_ASSEMBLY_ARRIVE("升降台成功到达指定层位",(byte) 0x09), // 
	SECURITY_DOOR_MOTOR_STUCK("防盗门马达卡死",(byte) 0x0A), // 
	SECURITY_DOOR_SUCCEED("防盗门成功打开或关闭",(byte) 0x0B), // 
	CABIN_DOOR_LOCK_ERROR("舱门锁错误",(byte) 0x0C), // 
	CABIN_DOOR_LOCK_SUCCEED("舱门锁控制成功",(byte) 0x0D), // 
	START_INFRARED_DETECTION_FAIL("启动红外检测失败",(byte) 0x0E) // 
	;

	private String name;
	private byte code;
	
	private ExeStatusEnum(String name,byte code) {
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
	public static ExeStatusEnum getEnumByNo(Byte no) {
		// 如果对象为空直接返回
		if (no == null) {
			return null;
		}
		ExeStatusEnum[] types = ExeStatusEnum.values();
		// 遍历枚举
		for (ExeStatusEnum temp : types) {
			// 当传入命令和枚举的命令相等
			if (temp.getCode()==no) {
				return temp;
			}
		}
		return null;
	}

}
