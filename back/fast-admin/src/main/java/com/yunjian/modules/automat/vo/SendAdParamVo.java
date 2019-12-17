package com.yunjian.modules.automat.vo;

import java.util.List;

public class SendAdParamVo {
	
	private List<Integer> adIdList;
	
	private List<String> deviceCodeList;
	
	public List<Integer> getAdIdList() {
		return adIdList;
	}
	public void setAdIdList(List<Integer> adIdList) {
		this.adIdList = adIdList;
	}
	public List<String> getDeviceCodeList() {
		return deviceCodeList;
	}
	public void setDeviceCodeList(List<String> deviceCodeList) {
		this.deviceCodeList = deviceCodeList;
	}
}
