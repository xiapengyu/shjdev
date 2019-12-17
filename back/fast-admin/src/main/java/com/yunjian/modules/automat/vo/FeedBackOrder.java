package com.yunjian.modules.automat.vo;

import java.util.ArrayList;
import java.util.List;

import com.yunjian.modules.automat.entity.OrderEntity;

public class FeedBackOrder extends OrderEntity {

	private static final long serialVersionUID = 1L;
	
	private String reason = "";
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	private List<OrderDetailExtDto> details = new ArrayList<>();

	public List<OrderDetailExtDto> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailExtDto> details) {
		this.details = details;
	};
	
}
