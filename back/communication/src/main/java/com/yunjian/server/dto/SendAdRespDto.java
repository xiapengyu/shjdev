package com.yunjian.server.dto;

import lombok.Data;

/**
 * 下发广告响应结果DTO
 */
@Data
public class SendAdRespDto {
	
	private String deviceCode;                  //机器编号
	private byte result;                        //设置结果
	
}
