package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 赠送规则
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Data
@TableName("tb_donate_rule")
public class DonateRuleEntity extends Model<DonateRuleEntity> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "donate_id", type = IdType.AUTO)
	private Integer donateId;

	private BigDecimal rechargeMoney;

	private BigDecimal donateMoney;

	private BigDecimal donateAmount;

	private String description;

	/**
	 * 赠送类型要细分下
	 */
	private Integer type;

	private Date startTime;

	private Date endTime;

	private Date createTime;

	/**
	 * 0无效,1有效
	 */
	private Integer enable;

	public Integer getDonateId() {
		return donateId;
	}

	public void setDonateId(Integer donateId) {
		this.donateId = donateId;
	}

	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public BigDecimal getDonateMoney() {
		return donateMoney;
	}

	public void setDonateMoney(BigDecimal donateMoney) {
		this.donateMoney = donateMoney;
	}

	public BigDecimal getDonateAmount() {
		return donateAmount;
	}

	public void setDonateAmount(BigDecimal donateAmount) {
		this.donateAmount = donateAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@Override
	protected Serializable pkVal() {
		return this.donateId;
	}

	@Override
	public String toString() {
		return "DonateRuleEntity{" + "donateId=" + donateId + ", rechargeMoney=" + rechargeMoney + ", donateMoney="
				+ donateMoney + ", donateAmount=" + donateAmount + ", description=" + description + ", type=" + type
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", createTime=" + createTime + ", enable="
				+ enable + "}";
	}
}
