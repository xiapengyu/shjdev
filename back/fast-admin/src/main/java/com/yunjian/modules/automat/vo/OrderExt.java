package com.yunjian.modules.automat.vo;

import java.util.List;

import com.yunjian.modules.automat.util.JsonUtil;

public class OrderExt{
	
	private List<GoodsDto> goodsList;
	
	private int payWay = 1;
	
	private String openId;
	
	private String mobile;
	
	private String deviceCode;
	
	private int orderId;
	
	private String orderCode;
	
	private double amount;
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public List<GoodsDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsDto> goodsList) {
		this.goodsList = goodsList;
	}

	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
	
}
