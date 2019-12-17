package com.yunjian.modules.automat.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GoodsDto{
	
	private Integer goodsId;
	
	private String goodsName;
	
	private BigDecimal unitPrice;
	
	private Integer amount;
}
