package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 经销商信息
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-22
 */
@TableName("tb_distributor")
public class DistributorEntity extends Model<DistributorEntity> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	private String name;

	private Integer level;

	/**
	 * 分销商比例
	 */
	private String ratio;

	/**
	 * 已结算提成
	 */
	private Double pushMoney;

	/**
	 * 未结算提成
	 */
	private Double notPushMoney;

	/**
	 * 累计结算提成
	 */
	private String totalPushMoney;

	/**
	 * 真实姓名
	 */
	private String personalName;

	/**
	 * 联系方式
	 */
	private String contact;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public Double getPushMoney() {
		return pushMoney;
	}

	public void setPushMoney(Double pushMoney) {
		this.pushMoney = pushMoney;
	}

	public Double getNotPushMoney() {
		return notPushMoney;
	}

	public void setNotPushMoney(Double notPushMoney) {
		this.notPushMoney = notPushMoney;
	}

	public String getTotalPushMoney() {
		return totalPushMoney;
	}

	public void setTotalPushMoney(String totalPushMoney) {
		this.totalPushMoney = totalPushMoney;
	}

	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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
		return "DistributorEntity{" + "id=" + id + ", name=" + name + ", level=" + level + ", ratio=" + ratio
				+ ", pushMoney=" + pushMoney + ", notPushMoney=" + notPushMoney + ", totalPushMoney=" + totalPushMoney
				+ ", personalName=" + personalName + ", contact=" + contact + ", createTime=" + createTime + "}";
	}
}
