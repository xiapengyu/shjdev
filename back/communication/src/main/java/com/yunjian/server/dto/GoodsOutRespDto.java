package com.yunjian.server.dto;

import lombok.Data;

/**
 * 下发广告响应结果DTO
 */
@Data
public class GoodsOutRespDto {
	
	private byte tradeType;                   //交易类型
	private short orderNoChanged;            //转换后订单号
	private byte flag;                        //标记码
	private String time;                      //时间戳
	private String deviceCode;                //机器编号
	
}
