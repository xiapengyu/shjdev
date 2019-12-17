package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 会员信息
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Data
@TableName("tb_member_info")
public class MemberInfoEntity extends Model<MemberInfoEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "member_id", type = IdType.AUTO)
    private Integer memberId;

    private String account;

    private String username;

    private String phoneNo;

    private Integer commodityCount;

    private Float balance;

    private Float donateMoney;

    /**
     * 0启用，1禁用
     */
    private Integer status;

    private Date registerTime;


    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Integer commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getDonateMoney() {
        return donateMoney;
    }

    public void setDonateMoney(Float donateMoney) {
        this.donateMoney = donateMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Override
    protected Serializable pkVal() {
        return this.memberId;
    }

    @Override
    public String toString() {
        return "MemberInfoEntity{" +
        "memberId=" + memberId +
        ", account=" + account +
        ", username=" + username +
        ", phoneNo=" + phoneNo +
        ", commodityCount=" + commodityCount +
        ", balance=" + balance +
        ", donateMoney=" + donateMoney +
        ", status=" + status +
        ", registerTime=" + registerTime +
        "}";
    }
}
