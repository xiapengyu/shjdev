package com.yunjian.modules.automat.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yunjian.modules.automat.service.RegionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DeviceMapper;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.util.DistanceHepler;
import com.yunjian.modules.automat.vo.Coordinate;
import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.automat.vo.PasswordVo;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-17
 */
@Service("deviceService")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {
	
	@Resource
	private DeviceMapper deviceMapper;

	@Autowired
	private RegionService regionService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<DeviceEntity>();
		if(StringUtils.isNotEmpty((String) params.get("deviceCode"))) {
			qw.eq("device_code", (String) params.get("deviceCode"));
		}else {
			if(StringUtils.isNotEmpty((String) params.get("province"))) {
				qw.eq("province", (String) params.get("province"));
			}
			if(StringUtils.isNotEmpty((String) params.get("city"))) {
				qw.eq("city", (String) params.get("city"));
			}
			if(StringUtils.isNotEmpty((String) params.get("distribute"))) {
				qw.eq("distribute", (String) params.get("distribute"));
			}
		}
        IPage<DeviceEntity> page = this.page(
                new Query<DeviceEntity>().getPage(params),
                qw
        );
        return new PageUtils(page);
    }

	@Override
	public int countTotal() {
		return this.count();
	}

	@Override
	public List<DeviceExt> queryDeviceListByCoordinateList(double lat, double lng) {
		List<DeviceEntity> list = list();
		double distance = 1.00;//1千米
		List<DeviceExt> nearbyDeviceList = Lists.newArrayList();
		for(DeviceEntity device : list) {
			DeviceExt ext = new DeviceExt();
			BeanUtils.copyProperties(device,ext);
			double d = DistanceHepler.distance(lat,lng, device.getLat().doubleValue(),device.getLng().doubleValue());
			ext.setDistance(DistanceHepler.distance(lng,lat,device.getLng().doubleValue(),device.getLat().doubleValue()));
			if(d <= distance){
				nearbyDeviceList.add(ext);
			}
		}
		return nearbyDeviceList;
	}

	@Override
	public List<DeviceEntity> queryDeviceListForMap(Map<String, Object> params) {
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<DeviceEntity>();
		if(StringUtils.isNotEmpty((String) params.get("deviceCode"))) {
			qw.eq("device_code", (String) params.get("deviceCode"));
		}else {
			if(StringUtils.isNotEmpty((String) params.get("province"))) {
				qw.eq("province", (String) params.get("province"));
			}
			if(StringUtils.isNotEmpty((String) params.get("city"))) {
				qw.eq("city", (String) params.get("city"));
			}
			if(StringUtils.isNotEmpty((String) params.get("distribute"))) {
				qw.eq("distribute", (String) params.get("distribute"));
			}
		}
		List<DeviceEntity> list = this.list(qw);
		return list;
	}

	@Override
	public List<DeviceExt> queryDeviceAll(Coordinate c) {
		List<DeviceEntity> deviceList = list();
		List<DeviceExt> list = Lists.newArrayList();
		for(DeviceEntity de : deviceList){
			DeviceExt ext = new DeviceExt();
			BeanUtils.copyProperties(de,ext);
			ext.setDistance(DistanceHepler.distance(c.getLng(),c.getLat(),de.getLng().doubleValue(),de.getLat().doubleValue()));

			String provinceName = regionService.getRegionName(de.getProvince());
			String cityName = regionService.getRegionName(de.getCity());
			String distributeName = regionService.getRegionName(de.getDistribute());
			ext.setProvinceName(provinceName);
			ext.setCityName(cityName);
			ext.setDistributeName(distributeName);

			list.add(ext);
		}
		List<DeviceExt> sortList = list.stream().sorted(Comparator.comparing(DeviceExt::getDistribute)).collect(Collectors.toList());
		return sortList;
	}

	@Override
	public DeviceEntity queryByAisleId(Integer aisleId) {
		return deviceMapper.queryByAisleId(aisleId);
	}

	@Override
	public List<DeviceEntity> queryByRegion(DeviceEntity device) {
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<>();
		if(device.getProvince() != null) {
			qw.eq("province", device.getProvince());
		}
		if(device.getCity() != null) {
			qw.eq("city", device.getCity());
		}
		if(device.getDistribute() != null) {
			qw.eq("distribute", device.getDistribute());
		}
		return list(qw);
	}

	@Override
	public boolean onlineSwitch(DeviceEntity device) {
		UpdateWrapper<DeviceEntity> uw = new UpdateWrapper<>();
		uw.eq("device_code", device.getDeviceCode());
		uw.set("online_status", device.getOnlineStatus());
		return update(uw);
	}

	@Override
	public boolean savePass(PasswordVo param) {
		UpdateWrapper<DeviceEntity> uw = new UpdateWrapper<>();
		uw.eq("device_code", param.getDeviceCode());
		if(param.getPasswordType() == 0 ) {
			uw.set("password", param.getPassword());
		}else {
			uw.set("test_password", param.getPassword());
		}
		
		return update(uw);
	}

}
