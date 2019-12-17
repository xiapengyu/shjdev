package com.yunjian.modules.automat.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-01
 */
@Data
@TableName("tb_ad")
public class AdEntity extends Model<AdEntity> {

private static final long serialVersionUID=1L;

    private Integer adId;

    private String url;

    private String title;

    /**
     * 轮播间隔
     */
    private Integer intervalTime;

    /**
     * 1启用，0禁用
     */
    private Integer status;

    private String content;

    private Date createTime;

    private Date startTime;

    private Date endTime;
    
    private String adType;

}
