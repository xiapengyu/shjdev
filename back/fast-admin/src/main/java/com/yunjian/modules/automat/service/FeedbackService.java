package com.yunjian.modules.automat.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.vo.FeedBackDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-12
 */
public interface FeedbackService extends IService<FeedbackEntity> {

	PageUtils queryPage(Map<String, Object> params);

	R saveFeedBack(FeedBackDto dto);

	PageUtils queryUserFeedBack(Map<String, Object> params);

}
