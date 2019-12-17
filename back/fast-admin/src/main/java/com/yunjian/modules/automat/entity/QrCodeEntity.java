package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-12
 */
@Data
@TableName("tb_qr_code")
public class QrCodeEntity extends Model<QrCodeEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "code_id", type = IdType.AUTO)
    private Integer codeId;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 生成时间
     */
    private Date createTime;


    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    protected Serializable pkVal() {
        return this.codeId;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return "QrCodeEntity{" +
        "codeId=" + codeId +
        ", deviceId=" + deviceId +
        ", batchNo=" + batchNo +
        ", qrCode=" + qrCode +
        ", createTime=" + createTime +
        "}";
    }
}
