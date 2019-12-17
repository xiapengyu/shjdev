package com.yunjian.modules.automat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 配送记录与商品关系表
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-02
 */
@TableName("tb_deliver_goods")
public class DeliverGoodsEntity extends Model<DeliverGoodsEntity> {

private static final long serialVersionUID=1L;

    private Integer deliverId;

    private Integer goodsId;


    public Integer getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(Integer deliverId) {
        this.deliverId = deliverId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    protected Serializable pkVal() {
        return this.deliverId;
    }

    @Override
    public String toString() {
        return "DeliverGoodsEntity{" +
        "deliverId=" + deliverId +
        ", goodsId=" + goodsId +
        "}";
    }
}
