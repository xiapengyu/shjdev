package com.yunjian.server.dto;

import lombok.Data;

/**
 * 软件升级-传输完成的响应DTO
 */
@Data
public class SendFinishDto {
	private String flag = "00000011";    //Flag, (byte)0x03的二进制字符串
	private String checksum;             //累加校验和, 1个字节的二进制字符串
	private String foreignChecksum;      //异域校验和，1个字节的二进制字符串
}
