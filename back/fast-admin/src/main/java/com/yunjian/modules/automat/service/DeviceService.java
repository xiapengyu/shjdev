package com.yunjian.modules.automat.service;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.DeviceEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.modules.automat.vo.Coordinate;
import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.automat.vo.PasswordVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
public interface DeviceService extends IService<DeviceEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	int countTotal();

	List<DeviceExt> queryDeviceListByCoordinateList(double lat, double lng);

	List<DeviceEntity> queryDeviceListForMap(Map<String, Object> params);

    List<DeviceExt> queryDeviceAll(Coordinate c);

	DeviceEntity queryByAisleId(Integer aisleId);

	List<DeviceEntity> queryByRegion(DeviceEntity device);

	boolean onlineSwitch(DeviceEntity device);

	boolean savePass(PasswordVo param);

}
