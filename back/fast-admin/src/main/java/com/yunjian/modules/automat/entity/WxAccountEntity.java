package com.yunjian.modules.automat.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 微信账号
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-15
 */
@Data
@TableName("tb_wx_account")
public class WxAccountEntity extends Model<WxAccountEntity> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String wxOpenId;

    private String sessionKey;

    private String phoneNo;

    private String password;
    
    /**
     * 是否免密。0:不免密，1免密
     */
    private Integer isNoPassword;

    /**
     * 是否勾选服务协议
     */
    private Integer isTick;

    private String headPic;

    private String nickName;

    private Integer gender;

    private String language;

    private String city;

    private String province;

    private String country;

    /**
     * 0:非配送员，1:配送员
     */
    private Integer type;

    /**
     * 余额
     */
    private BigDecimal amount = BigDecimal.valueOf(0);

    /**
     * 赠送金额
     */
    private BigDecimal donate = BigDecimal.valueOf(0);

    /**
     * 用户余额签名
     */
    private String sign;

    /**
     * 用户积分
     */
    private Integer integral = 0;
    
    private Date createTime;
    
    private String identityNo;
}
