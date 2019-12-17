package com.yunjian.server.api.vo;

import java.util.List;

import lombok.Data;

@Data
public class PullLogParamVo {
	
	private List<String> deviceCodeList;
	private int logType;

}
