package com.yunjian.modules.automat.vo;

import com.yunjian.modules.automat.entity.OrderDetailEntity;

public class OrderDetailExtDto extends OrderDetailEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String imgPath;

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
