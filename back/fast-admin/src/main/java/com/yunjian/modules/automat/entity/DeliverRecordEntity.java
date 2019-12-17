package com.yunjian.modules.automat.entity;

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
 * @since 2019-06-16
 */
@Data
@TableName("tb_deliver_record")
public class DeliverRecordEntity extends Model<DeliverRecordEntity> {

private static final long serialVersionUID=1L;

    /**
     * 配送记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配送员手机号
     */
    private String phoneNo;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 上架数
     */
    private Integer upAmount;

    /**
     * 下架数
     */
    private Integer downAmount;

    /**
     * 总数
     */
    private Integer totalAmount;

    private Date createTime;

    
}
