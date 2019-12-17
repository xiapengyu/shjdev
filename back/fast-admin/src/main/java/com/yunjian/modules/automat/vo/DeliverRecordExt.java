package com.yunjian.modules.automat.vo;

import com.yunjian.modules.automat.entity.DeliverRecordEntity;

import lombok.Data;

@Data
public class DeliverRecordExt extends DeliverRecordEntity {
	
	private String deliverName;
	
	private DeviceExt device;
	
}
