package com.yunjian.server.dto;

import lombok.Data;

@Data
public class StatusCode3 {
	
	private Integer bit0;      //第 1 种洗衣液，A 桶。0  有液，1 无液 
	private Integer bit1;      //第 1 种洗衣液，B 桶。0  有液，1 无液
	private Integer bit2;      //第 2 种洗衣液，A 桶。0  有液，1 无液 
	private Integer bit3;      //第 2 种洗衣液，B 桶。0  有液，1 无液
	private Integer bit4;      //第3 种洗衣液，A 桶。0  有液，1 无液 
	private Integer bit5;      //第 3 种洗衣液，B 桶。0  有液，1 无液
	private Integer bit6;      //第 4 种洗衣液，A 桶。0  有液，1 无液 
	private Integer bit7;      //第 4 种洗衣液，B 桶。0  有液，1 无液

}
