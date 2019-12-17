package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 预警管理
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Data
@TableName("tb_device_warn")
public class DeviceWarnEntity extends Model<DeviceWarnEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 设备表主键
	 */
	@TableId(value = "device_id", type = IdType.AUTO)
	private Integer deviceId;

	/**
	 * 设备编码
	 */
	private String deviceCode;

	/**
	 * mac地址
	 */
	private String macAddress;

	/**
	 * 设备状态0:启用 1禁用
	 */
	private Integer status;

	/**
	 * 省
	 */
	private Integer province;

	/**
	 * 市
	 */
	private Integer city;

	/**
	 * 区
	 */
	private Integer distribute;

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 经度
	 */
	private BigDecimal lng;

	/**
	 * 维度
	 */
	private BigDecimal lat;

	/**
	 * 设备注册时间
	 */
	private Date registerTime;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	private Integer hasDeliverPeople;
	private String street;
}
