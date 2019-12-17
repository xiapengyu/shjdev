package com.yunjian.server.api.vo;

import lombok.Data;

@Data
public class ConfigTempParamVo {
	
	private String deviceCode;
	private int sign;
	private int lockerNo;
	private int temp;

}
