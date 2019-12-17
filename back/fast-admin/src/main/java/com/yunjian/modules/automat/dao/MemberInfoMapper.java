package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.MemberInfoEntity;

/**
 * <p>
 * 会员信息 Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Mapper
public interface MemberInfoMapper extends BaseMapper<MemberInfoEntity> {

}
