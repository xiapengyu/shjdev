package com.yunjian.modules.automat.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.util.JsonUtil;

public class FeedBackDto extends FeedbackEntity{

	private static final long serialVersionUID = 1L;
	
	private List<String> imageList = new ArrayList<String>();
	
	private String operation;
	
	private List<Integer> payStatusList;
	
	public List<Integer> getPayStatusList() {
		return payStatusList;
	}

	public void setPayStatusList(List<Integer> payStatusList) {
		this.payStatusList = payStatusList;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String toString() {
		return JsonUtil.toJsonString(this);
	}
}
