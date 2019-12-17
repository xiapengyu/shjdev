package com.yunjian.modules.automat.vo;

import lombok.Data;

@Data
public class Coordinate {
	/**
	 * 	纬度
	 */
	private Double lat;


	/**
	 * 	经度
	 */
	private Double lng;
	
	private String deviceCode;

}
