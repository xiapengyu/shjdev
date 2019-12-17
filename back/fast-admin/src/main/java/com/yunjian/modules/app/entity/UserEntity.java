package com.yunjian.modules.app.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Data
@TableName("tb_user")
public class UserEntity extends Model<UserEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户余额
     */
    private Float amount;

    /**
     * 赠送余额
     */
    private Float donateAmount;

    /**
     * 余额校验签名
     */
    private String sign;

    private Integer status;

    /**
     * 是否配送员
     */
    private Integer isDeliver;

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getDonateAmount() {
        return donateAmount;
    }

    public void setDonateAmount(Float donateAmount) {
        this.donateAmount = donateAmount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeliver() {
        return isDeliver;
    }

    public void setIsDeliver(Integer isDeliver) {
        this.isDeliver = isDeliver;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
        "userId=" + userId +
        ", account=" + account +
        ", username=" + username +
        ", mobile=" + mobile +
        ", password=" + password +
        ", amount=" + amount +
        ", donateAmount=" + donateAmount +
        ", sign=" + sign +
        ", status=" + status +
        ", isDeliver=" + isDeliver +
        ", createTime=" + createTime +
        "}";
    }
}
