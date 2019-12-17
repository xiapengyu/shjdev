package com.yunjian.api.dto;

import lombok.Data;

/**
 * 读版本信息的响应信息中，支持功能的第一个字节，其他字节目前默认全0，
 * 如需扩展，则横向添加VersionSupportCode2,...,即可。
 */
@Data
public class VersionSupportCode1 {
	private Integer bit0;      //1 支持出货检测（如红外），0 不支持 
	private Integer bit1;      //1 支持玻璃门开关检测，0 不支持 
	private Integer bit2;      //1 支持升降机功能，0 不支持
	private Integer bit3;      //1 电子防盗挡板，0 机械防盗挡板 
	private Integer bit4;      //1 支持仓门开关检测，0 不支持  (升降机、成人用品机用) 
	private Integer bit5;      //1 支持仓门锁，0 不支持  (升降机、成人用品机用) 
	private Integer bit6;      //1 支持读温度，0 不支持 
	private Integer bit7;      //1 支持升级，0 不支持 
}
