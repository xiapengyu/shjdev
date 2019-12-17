package com.yunjian.modules.automat.vo;

import com.yunjian.modules.automat.entity.DeviceEntity;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class DeviceExt extends DeviceEntity {
	private String provinceName;
	private String cityName;
	private String distributeName;
	private Double distance;
	private Integer hasDeliverPeople;
}
