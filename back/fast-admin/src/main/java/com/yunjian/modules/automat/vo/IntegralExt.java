package com.yunjian.modules.automat.vo;

import java.util.List;

import com.yunjian.modules.automat.entity.IntegralEntity;

import lombok.Data;

@Data
public class IntegralExt extends IntegralEntity {
	
	private Integer totalScore; //总积分
	
	private List<IntegralEntity> list;

}
