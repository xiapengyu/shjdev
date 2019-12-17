package com.yunjian.modules.automat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.AdvertisementEntity;

import java.util.Map;

public interface AdvertisementService extends IService<AdvertisementEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void statusChange(Integer status);

}
