package com.yunjian.modules.automat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 货道信息管理表
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-01
 */
@Data
@TableName("tb_goods_aisle")
public class GoodsAisleEntity extends Model<GoodsAisleEntity> {

private static final long serialVersionUID=1L;

    private Integer aisleId;

    private String deviceCode;

    /**
     * 柜子号
     */
    private Integer lockerNo;

    /**
     * 货道号
     */
    private Integer aisleNo;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品数量
     */
    private Integer goodsQuantity;
    
}
