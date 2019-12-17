package com.yunjian.modules.automat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@TableName("tb_region")
public class RegionEntity extends Model<RegionEntity> {

private static final long serialVersionUID=1L;

    /**
     * 行政区域id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 行政区域名称
     */
    private String name;

    /**
     * 上级行政区域id
     */
    private Integer parentId;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 级别2省份3市区4区县
     */
    private Integer levelType;

    private String cityCode;

    /**
     * 邮编
     */
    private String zipCode;

    private String mergeName;

    private String pinyin;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RegionEntity{" +
        "id=" + id +
        ", name=" + name +
        ", parentId=" + parentId +
        ", shortName=" + shortName +
        ", levelType=" + levelType +
        ", cityCode=" + cityCode +
        ", zipCode=" + zipCode +
        ", mergeName=" + mergeName +
        ", pinyin=" + pinyin +
        ", lng=" + lng +
        ", lat=" + lat +
        "}";
    }
}
