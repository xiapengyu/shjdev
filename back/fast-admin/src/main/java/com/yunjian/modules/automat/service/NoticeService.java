package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.NoticeEntity;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-08
 */
public interface NoticeService extends IService<NoticeEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
