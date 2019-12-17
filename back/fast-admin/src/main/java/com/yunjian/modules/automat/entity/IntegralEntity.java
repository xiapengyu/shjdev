package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 积分信息
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@TableName("tb_integral")
public class IntegralEntity extends Model<IntegralEntity> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 分数，正负数
     */
    private Integer score;
    
    /**
     * 金额
     */
    private Float money;

    /**
     * 来源1充值(充值记录id)，2消费(消费记录id)
     */
    private Integer source;

    /**
     * 类型：1充值，2消费
     */
    private Integer type;

    /**
     * 记录产生时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
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
        return "IntegralEntity{" +
        "id=" + id +
        ", phoneNo=" + phoneNo +
        ", score=" + score +
        ", source=" + source +
        ", type=" + type +
        ", createTime=" + createTime +
        "}";
    }
}
