package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DonateRecordEntity;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 赠送记录 服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-03
 */
public interface DonateRecordService extends IService<DonateRecordEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
