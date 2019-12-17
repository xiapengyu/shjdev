package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DistributorEntity;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 经销商信息 服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-16
 */
public interface DistributorService extends IService<DistributorEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
