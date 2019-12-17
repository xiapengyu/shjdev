package com.yunjian.modules.automat.vo;

import com.yunjian.modules.automat.entity.GoodsEntity;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class GoodsExt extends GoodsEntity {
	private Integer goodsQuantity;
	private Integer needQuantity;
}
