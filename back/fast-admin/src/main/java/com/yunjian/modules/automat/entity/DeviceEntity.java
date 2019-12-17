package com.yunjian.modules.automat.entity;

import java.io.Serializable;
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
 * @author laizhiwen
 * @since 2019-07-30
 */
@Data
@TableName("tb_device")
public class DeviceEntity extends Model<DeviceEntity> {

private static final long serialVersionUID=1L;

    /**
     * 设备表主键
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Integer deviceId;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * 设备状态0:启用 1禁用，2预警、3离线
     */
    private Integer status;
    
    private Integer onlineStatus;

    /**
     * 省
     */
    private Integer province;

    /**
     * 市
     */
    private Integer city;

    /**
     * 区
     */
    private Integer distribute;

    /**
     * 详细地址
     */
    private String address;

    private String street;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 设备注册时间
     */
    private Date registerTime;

    private String password;
    private String testPassword;
}
