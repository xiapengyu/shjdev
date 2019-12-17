package com.yunjian.modules.automat.vo;

import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AisleExt extends GoodsAisleEntity {
	private GoodsEntity goods;
}
