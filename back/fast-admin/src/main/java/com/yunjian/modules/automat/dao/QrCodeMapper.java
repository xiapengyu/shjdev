

package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.QrCodeEntity;

/**
 * 二维码
 */
@Mapper
public interface QrCodeMapper extends BaseMapper<QrCodeEntity> {

}
