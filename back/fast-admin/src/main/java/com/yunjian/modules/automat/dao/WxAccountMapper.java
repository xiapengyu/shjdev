package com.yunjian.modules.automat.dao;

import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 微信账号 Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-07-08
 */
@Mapper
public interface WxAccountMapper extends BaseMapper<WxAccountEntity> {

	String getNameByPhone(String phoneNo);

}
