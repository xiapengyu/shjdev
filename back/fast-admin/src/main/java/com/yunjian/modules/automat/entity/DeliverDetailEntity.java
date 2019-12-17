package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-02
 */
@TableName("tb_deliver_detail")
public class DeliverDetailEntity extends Model<DeliverDetailEntity> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private Integer recordId;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 包装单位
	 */
	private String unit;

	/**
	 * 数量
	 */
	private Integer amount;

	/**
	 * 单价
	 */
	private Float goodsPrice;

	/**
	 * 总价
	 */
	private Float totalPrice;

	/**
	 * 设备编码
	 */
	private String deviceCode;

	/**
	 * 备注
	 */
	private String remark;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DeliverDetailEntity{" + "id=" + id + ", recordId=" + recordId + ", goodsName=" + goodsName + ", unit="
				+ unit + ", amount=" + amount + ", goodsPrice=" + goodsPrice + ", totalPrice=" + totalPrice
				+ ", deviceCode=" + deviceCode + ", remark=" + remark + ", createTime=" + createTime + "}";
	}
}
