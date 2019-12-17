package com.yunjian.modules.automat.vo;

public class UploadGoodsOutVo {

	private String result;		//出货结果 0失败  1成功
	
	private String deviceCode;	//设备编码
	
	private String orderNo;			//订单号
	
	private int lockerNo;             //1byte,柜子号
	
	private int aisle;             //1byte，货道号

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

	public int getLockerNo() {
		return lockerNo;
	}

	public void setLockerNo(int lockerNo) {
		this.lockerNo = lockerNo;
	}

	public int getAisle() {
		return aisle;
	}

	public void setAisle(int aisle) {
		this.aisle = aisle;
	}

}
