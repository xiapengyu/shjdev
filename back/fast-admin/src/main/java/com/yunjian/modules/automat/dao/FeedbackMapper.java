package com.yunjian.modules.automat.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.FeedbackEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-15
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<FeedbackEntity> {
	
	public int saveFeedBack(FeedbackEntity entity);

}
