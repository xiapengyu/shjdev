package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 充值记录
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Data
@TableName("tb_recharge_record")
public class RechargeRecordEntity extends Model<RechargeRecordEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Integer rechargeId;
    private Integer integral; //积分

    private String wechatOrderNo;

    private String venderOrderNo;

    private String phoneNo;

    /**
     * 状态细分
     */
    private Integer status;

    private BigDecimal totalMoney;

    private BigDecimal donateMoney;

    private BigDecimal cash;

    private Date createTime;

    private Date payTime;
}
