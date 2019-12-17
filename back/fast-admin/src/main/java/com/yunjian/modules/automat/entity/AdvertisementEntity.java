

package com.yunjian.modules.automat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告
 *
 *
 */
@Data
@TableName("tb_ad")
public class AdvertisementEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long adId;

	private Integer picturePath;

	private String title;

	private Integer status;
	
	private String content;

	private Date createTime;

	private Date startTime;

	private Date endTime;

}
