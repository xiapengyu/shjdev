package com.yunjian.api.dto;

import com.yunjian.modules.automat.entity.WxAccountEntity;

public class LoginDto extends WxAccountEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String verifyCode;
	
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}
