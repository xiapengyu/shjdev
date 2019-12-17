package com.yunjian.server.api.vo;

import java.util.List;

import lombok.Data;

@Data
public class SendAdParamVo {
	
	private List<Integer> adIdList;
	
	private List<String> deviceCodeList;

}
