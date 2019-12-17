package com.yunjian.modules.automat.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 预警设备与会员的关系
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Data
@TableName("tb_device_member")
public class DeviceMemberEntity extends Model<DeviceMemberEntity> {

private static final long serialVersionUID=1L;

    private String deviceCode;

    private String phoneNo;

}
