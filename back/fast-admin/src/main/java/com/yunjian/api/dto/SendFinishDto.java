package com.yunjian.api.dto;

import lombok.Data;

/**
 * 软件升级-传输完成的响应DTO
 */
@Data
public class SendFinishDto {
	private byte flag = (byte)0x03;    //Flag
	private byte checksum;             //累加校验和
	private byte foreignChecksum;      //异域校验和
}
