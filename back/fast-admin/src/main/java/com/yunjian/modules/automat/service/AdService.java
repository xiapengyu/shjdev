package com.yunjian.modules.automat.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.AdEntity;

public interface AdService extends IService<AdEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void statusChange(AdEntity ad);

	List<AdEntity> adListForDevice(List<Integer> ids);

}
