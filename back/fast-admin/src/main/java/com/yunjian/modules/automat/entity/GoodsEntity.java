package com.yunjian.modules.automat.entity;

import java.math.BigDecimal;
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
 * @since 2019-06-20
 */
@Data
@TableName("tb_goods")
public class GoodsEntity extends Model<GoodsEntity> {

private static final long serialVersionUID=1L;

    /**
     * 商品id
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;
    
    /**
     * 包装单位
     */
    private String unit;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;

    /**
     * 图片路径
     */
    private String imgPath;

    /**
     * 商品状态1销售中2已下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}
