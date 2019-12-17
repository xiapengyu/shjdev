package com.yunjian.server.dto;

import lombok.Data;

/**
 * 远程设置柜子灯光响应结果DTO
 */
@Data
public class SetDeviceTempRespDto {
	
	private String deviceCode;                  //机器编号
	private byte result;                        //设置结果
	
}
