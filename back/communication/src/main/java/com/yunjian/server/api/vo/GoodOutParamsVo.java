package com.yunjian.server.api.vo;

import lombok.Data;

/**
 * 出货相关参数 
 */
@Data
public class GoodOutParamsVo {
	
	private String deviceCode;
	private int lockerNo;             //1byte,柜子号
	private int aisle;             //1byte，货道号
	private int goodsCount;           //1byte,联动数量
	private int runTime;             //1byte,执行时间
	private int direction;           //1byte,电机转动方向
	private String orderNo;           //14字节,订单号
	private String time;              //7字节,BCD码，时间戳
	
}
