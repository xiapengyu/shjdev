package com.yunjian.server.api.vo;

import java.util.List;

import lombok.Data;

@Data
public class UpgradeParamVo {
	
	private List<String> deviceCodeList;
	private int appType;
	private String appVersion;
	private String md5;

}
