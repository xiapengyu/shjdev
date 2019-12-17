package com.yunjian.modules.automat.entity;

import java.io.Serializable;
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
 * @since 2019-08-09
 */
@TableName("tb_feedback")
public class FeedbackEntity extends Model<FeedbackEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Integer feedbackId;

    private String phone;

    private String content;

    private String image;

    private String orderCode;

    /**
     * 1待审核2已处理3已驳回
     */
    private Integer status;

    /**
     * 反馈类型1意见反馈2退款反馈
     */
    private Integer type;

    /**
     * 处理说明
     */
    private String reason;

    private Date updateTime;

    private Date createTime;

    private String updateUser;


    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.feedbackId;
    }

    @Override
    public String toString() {
        return "FeedbackEntity{" +
        "feedbackId=" + feedbackId +
        ", phone=" + phone +
        ", content=" + content +
        ", image=" + image +
        ", orderCode=" + orderCode +
        ", status=" + status +
        ", type=" + type +
        ", reason=" + reason +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        "}";
    }
}
