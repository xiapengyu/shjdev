package com.yunjian.modules.automat.vo;

public class UploadCartOutVo {

	private String result;		//出货结果 => 柜子号,货道,成功失败,错误码;柜子号,货道,成功失败,错误码;柜子号,货道,成功失败,错误码  （1表示成功，0表示失败）
	
	private String deviceCode;	//设备编码
	
	private String orderNo;			//订单号
	

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
