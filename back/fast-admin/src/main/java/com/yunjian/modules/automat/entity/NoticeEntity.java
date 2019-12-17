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
 * @since 2019-08-18
 */
@TableName("tb_notice")
public class NoticeEntity extends Model<NoticeEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;

    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 1启用 0禁用
     */
    private Integer status;

    private Date createTime;

    private String createUser;


    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.noticeId;
    }

    @Override
    public String toString() {
        return "NoticeEntity{" +
        "noticeId=" + noticeId +
        ", title=" + title +
        ", content=" + content +
        ", status=" + status +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        "}";
    }
}
