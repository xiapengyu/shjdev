package com.yunjian.modules.automat.vo;

import java.io.Serializable;

public class RechargeReqDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// 用户手机号
	private String mobile;
	// 支付金额
	private String amount;
	
	private String payWay = "wechat";
	// 用户端IP
	private String terminalIp;

	private String subject; // 255 Y 订单描述关键字等

	private String tradeType = "JSAPI"; // 32 N 支付类型（默认）APP: 手机app支付 MWEB：H5支付

	private String openid; // 128
							// trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换

	private String actId; // 充值活动id,此参数非必填,未参数活动字段为空

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
}
