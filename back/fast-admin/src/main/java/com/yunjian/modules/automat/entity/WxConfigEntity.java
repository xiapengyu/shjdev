package com.yunjian.modules.automat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-16
 */
@TableName("tb_wx_config")
public class WxConfigEntity extends Model<WxConfigEntity> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private String uuid;

    private String appId;

    private String mchId;

    private String mchKey;

    /**
     * 充值回调
     */
    private String rechargeNotify;

    /**
     * 下单支付回调
     */
    private String orderNotify;

    /**
     * 退款结果回调
     */
    private String refundNotify;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getRechargeNotify() {
        return rechargeNotify;
    }

    public void setRechargeNotify(String rechargeNotify) {
        this.rechargeNotify = rechargeNotify;
    }

    public String getOrderNotify() {
        return orderNotify;
    }

    public void setOrderNotify(String orderNotify) {
        this.orderNotify = orderNotify;
    }

    public String getRefundNotify() {
        return refundNotify;
    }

    public void setRefundNotify(String refundNotify) {
        this.refundNotify = refundNotify;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "WxConfigEntity{" +
        "uuid=" + uuid +
        ", appId=" + appId +
        ", mchId=" + mchId +
        ", mchKey=" + mchKey +
        ", rechargeNotify=" + rechargeNotify +
        ", orderNotify=" + orderNotify +
        ", refundNotify=" + refundNotify +
        "}";
    }
}
