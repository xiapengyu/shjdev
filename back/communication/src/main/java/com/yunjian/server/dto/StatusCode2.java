package com.yunjian.server.dto;

import lombok.Data;

@Data
public class StatusCode2 {
	
	private Integer bit0;      //1 玻璃门打开，0 玻璃门关闭 
	private Integer bit1;      //1 防盗门打开（非防盗状态），0 防盗门关闭（防盗状态）
	private Integer bit2;      //1 升降托盘（取货口）有货，0 无货  (升降机、成人用品机、洗衣液售货机用)
	private Integer bit3;      //1 仓门打开，0 仓门关闭  (升降机、成人用品机、洗衣液售货机用) 
	private Integer bit4;      //：1 仓门锁打开，0 仓门锁关闭  (升降机、成人用品机、洗衣液售货机用)
	private Integer bit5;      //电子秤是否开机并连接正常    0 正常  1 不正常  (洗衣液售货机用)
	private Integer bit6;      //电子秤是否偏差大      0 无  1 有  (洗衣液售货机用)
	private Integer bit7;      //电子秤上是否放上了空瓶    0 无  1 有  (洗衣液售货机用 8) 

}
