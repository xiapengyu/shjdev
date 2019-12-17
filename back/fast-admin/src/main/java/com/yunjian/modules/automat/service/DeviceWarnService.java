package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.modules.automat.vo.DeviceExt;

/**
 * <p>
 * 预警管理 服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
public interface DeviceWarnService extends IService<DeviceWarnEntity> {
	
	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 根据phoneNo查找告警设备信息
	 * @param phoneNo
	 * @param lng
	 * @param lat
	 * @return
	 */
	List<DeviceExt> findDeviceWarningByPhoneNo(String phoneNo, Double lng, Double lat);

	void saveWarnDevice(List<String> deviceCodes);

}
