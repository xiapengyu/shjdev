package com.yunjian.api.dto;

import lombok.Data;

@Data
public class VersionDto {
	
	/**
	 * 版本号
	 */
	private int version;
	
	/**
	 * 1 字节，用二进制字符串表示
		1  弹簧货道  (0x01)
		2  电子锁货道 (0x02)
		3  洗发水货道 (0x03)
		4  履带+弹簧货道 (0x04)
	 */
	private String aisleType; 
	
	/**
	 * 4 字节，用16进制字符串表示
		bit0：1 支持出货检测（如红外），0 不支持 
		bit1：1 支持玻璃门开关检测，0 不支持 
		bit2：1 支持升降机功能，0 不支持 
		bit3：1 电子防盗挡板，0 机械防盗挡板 
		bit4：1 支持仓门开关检测，0 不支持  (升降机、成人用品机用) 
		bit5：1 支持仓门锁，0 不支持  (升降机、成人用品机用) 
		bit6:    1 支持读温度，0 不支持 
		bit7:    1 支持升级，0 不支持 
		其它：0 
	 */
	private VersionSupportCode1 support;
	
	private String support2 = "00000000"; //第二个字节的二进制字符串
	private String support3 = "00000000"; //第三个字节的二进制字符串
	private String support4 = "00000000"; //第四个字节的二进制字符串
}
