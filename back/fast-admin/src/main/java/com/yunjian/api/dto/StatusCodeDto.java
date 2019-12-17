package com.yunjian.api.dto;

import lombok.Data;

@Data
public class StatusCodeDto {
	
	private String code1;     //1字节，对应的2进制字符串
	private StatusCode2 code2;
	private StatusCode3 code3;
	private String code4 = "00000000"; //1字节，对应的2进制字符串
	private String code5 = "00000000"; //1字节，对应的2进制字符串
	private String code6 = "00000000"; //1字节，对应的2进制字符串

}
