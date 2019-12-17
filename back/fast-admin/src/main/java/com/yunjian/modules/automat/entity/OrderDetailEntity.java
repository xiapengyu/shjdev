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
 * @since 2019-07-31
 */
@TableName("tb_order_detail")
public class OrderDetailEntity extends Model<OrderDetailEntity> {

private static final long serialVersionUID=1L;

    /**
     * 详情id
     */
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Integer detailId;

    /**
     * 订单id
     */
    private Integer orderId;

    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private Float goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsAmount;

    /**
     * 时间
     */
    private Date createTime;


    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Float getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Float goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    protected Serializable pkVal() {
        return this.detailId;
    }

    @Override
    public String toString() {
        return "OrderDetailEntity{" +
        "detailId=" + detailId +
        ", orderId=" + orderId +
        ", goodsId=" + goodsId +
        ", goodsName=" + goodsName +
        ", goodsPrice=" + goodsPrice +
        ", goodsAmount=" + goodsAmount +
        ", createTime=" + createTime +
        "}";
    }
}
