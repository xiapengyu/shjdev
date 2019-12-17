package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 赠送记录
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-20
 */
@Data
@TableName("tb_donate_record")
public class DonateRecordEntity extends Model<DonateRecordEntity> {

private static final long serialVersionUID=1L;

    private Integer recordId;

    /**
     * 赠送类型要细分下
     */
    private Integer type;

    private String phoneNo;

    private BigDecimal rechargeMoney;

    private BigDecimal donateMoney;

    private Date donateTime;

    private String wechatOrderNo;


    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public Date getDonateTime() {
		return donateTime;
	}

	public void setDonateTime(Date donateTime) {
		this.donateTime = donateTime;
	}

	public String getWechatOrderNo() {
        return wechatOrderNo;
    }

    public void setWechatOrderNo(String wechatOrderNo) {
        this.wechatOrderNo = wechatOrderNo;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        return "DonateRecordEntity{" +
        "recordId=" + recordId +
        ", type=" + type +
        ", phoneNo=" + phoneNo +
        ", rechargeMoney=" + rechargeMoney +
        ", donateMoney=" + donateMoney +
        ", donateTime=" + donateTime +
        ", wechatOrderNo=" + wechatOrderNo +
        "}";
    }
}
