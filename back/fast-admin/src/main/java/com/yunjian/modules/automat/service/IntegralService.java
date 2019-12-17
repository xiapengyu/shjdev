package com.yunjian.modules.automat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.IntegralEntity;

/**
 * <p>
 * 积分信息 服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-22
 */
public interface IntegralService extends IService<IntegralEntity> {

	R getIntegralInfoList(String phoneNo);
	
}
