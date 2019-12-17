package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-11
 */
@Data
@TableName("tb_work_order")
public class WorkOrderEntity extends Model<WorkOrderEntity> {

private static final long serialVersionUID=1L;

    private Integer workId;

    private Integer feedbackType;

    private String orderNo;

    private String picturePath;

    private String content;

    /**
     * 需细分
     */
    private Integer status;

    private String reason;

    private Date createTime;

    private Date startTime;

    private Date endTime;


    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Override
    protected Serializable pkVal() {
        return this.workId;
    }

    @Override
    public String toString() {
        return "WorkOrderEntity{" +
        "workId=" + workId +
        ", feedbackType=" + feedbackType +
        ", orderNo=" + orderNo +
        ", picturePath=" + picturePath +
        ", content=" + content +
        ", status=" + status +
        ", reason=" + reason +
        ", createTime=" + createTime +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        "}";
    }
}
