package com.yunjian.modules.automat.vo;

import java.util.List;

import com.yunjian.modules.automat.entity.GoodsAisleEntity;

import lombok.Data;

@Data
public class ReplenishmentVo {
	private String phoneNo;
	private String deviceCode;
	private List<Detail> detail;
	
	@Data
	public static class Detail{
		private Integer goodsId;
		private Integer goodsQuantity;
	}
}

