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
 * @since 2019-08-19
 */
@TableName("tb_order")
public class OrderEntity extends Model<OrderEntity> {

private static final long serialVersionUID=1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    private String weixinOrderNo;

    /**
     * 手机号码
     */
    private String phoneNo;

    /**
     * 订单使用金额
     */
    private Float totalAmount;

    /**
     * 商品数量
     */
    private Integer goodsAmount;

    /**
     * 支付状态0待支付1已支付2退款中3被驳回4已退款5退款失败6已取货
     */
    private Integer payStatus;

    /**
     * 支付方式1微信支付2钱包
     */
    private Integer payWay;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 申请退款时间
     */
    private Date arefundTime;

    /**
     * 退款驳回时间
     */
    private Date rejectTime;

    /**
     * 退款成功时间
     */
    private Date refundTime;

    /**
     * 扫码取货时间
     */
    private Date sweepTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单积分
     */
    private Integer integral;

    private String deviceCode;

    /**
     * 订单二维码
     */
    private String qrCode;

    /**
     * 订单二维码图片
     */
    private String qrCodeImage;

    /**
     * 订单使用赠送金额
     */
    private Float donateAmount;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private Float refundAmount;

    /**
     * 订单编码，10位长度
     */
    private String orderCode;

    /**
     * 出货成功数量
     */
    private Integer outSuccess;
    
    /**
     * 出货失败数量
     */
    private Integer outFail;

    public Integer getOutFail() {
		return outFail;
	}

	public void setOutFail(Integer outFail) {
		this.outFail = outFail;
	}

	public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWeixinOrderNo() {
        return weixinOrderNo;
    }

    public void setWeixinOrderNo(String weixinOrderNo) {
        this.weixinOrderNo = weixinOrderNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getArefundTime() {
		return arefundTime;
	}

	public void setArefundTime(Date arefundTime) {
		this.arefundTime = arefundTime;
	}

	public Date getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Date getSweepTime() {
		return sweepTime;
	}

	public void setSweepTime(Date sweepTime) {
		this.sweepTime = sweepTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public Float getDonateAmount() {
        return donateAmount;
    }

    public void setDonateAmount(Float donateAmount) {
        this.donateAmount = donateAmount;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Float getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Float refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOutSuccess() {
        return outSuccess;
    }

    public void setOutSuccess(Integer outSuccess) {
        this.outSuccess = outSuccess;
    }

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
        "orderId=" + orderId +
        ", orderNo=" + orderNo +
        ", weixinOrderNo=" + weixinOrderNo +
        ", phoneNo=" + phoneNo +
        ", totalAmount=" + totalAmount +
        ", goodsAmount=" + goodsAmount +
        ", payStatus=" + payStatus +
        ", payWay=" + payWay +
        ", payTime=" + payTime +
        ", arefundTime=" + arefundTime +
        ", rejectTime=" + rejectTime +
        ", refundTime=" + refundTime +
        ", sweepTime=" + sweepTime +
        ", createTime=" + createTime +
        ", integral=" + integral +
        ", deviceCode=" + deviceCode +
        ", qrCode=" + qrCode +
        ", qrCodeImage=" + qrCodeImage +
        ", donateAmount=" + donateAmount +
        ", refundNo=" + refundNo +
        ", refundAmount=" + refundAmount +
        ", orderCode=" + orderCode +
        ", outSuccess=" + outSuccess +
        "}";
    }
}
