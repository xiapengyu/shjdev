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
 * @since 2019-08-24
 */
@TableName("tb_verify_code")
public class VerifyCodeEntity extends Model<VerifyCodeEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    private String code;

    private Integer isRead;

    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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
        return "VerifyCodeEntity{" +
        "id=" + id +
        ", phone=" + phone +
        ", code=" + code +
        ", isRead=" + isRead +
        ", createTime=" + createTime +
        "}";
    }
}
