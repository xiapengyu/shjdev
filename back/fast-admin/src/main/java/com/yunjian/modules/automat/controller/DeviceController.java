package com.yunjian.modules.automat.controller;


import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.RegionEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.util.GPS;
import com.yunjian.modules.automat.util.GPSConverterUtils;
import com.yunjian.modules.automat.vo.Coordinate;
import com.yunjian.modules.automat.vo.DeviceExt;
import com.yunjian.modules.automat.vo.PasswordVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-17
 */
@Slf4j
@Api(tags = "设备相关接口")
@RestController
@RequestMapping("/automat/device")
public class DeviceController extends AbstractController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private RegionService regionService;
	
	@Value("${communication.base.path}")
	private String communicationBasePath;

	/**
	 * 所有设备列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@GetMapping("/list")
	@RequiresPermissions("device:list")
	public R list(@RequestParam Map<String, Object> params) throws IllegalAccessException, InvocationTargetException{
		List<RegionEntity> provinceList = regionService.getProvinceList();
		R r = R.ok().put("provinceList", provinceList);
		PageUtils page = deviceService.queryPage(params);
		@SuppressWarnings("unchecked")
		List<DeviceEntity> list = (List<DeviceEntity>) page.getList();
		List<DeviceExt> listExt = Lists.newArrayList();
		for(DeviceEntity d : list) {
			DeviceExt deviceExt = new DeviceExt();
			BeanUtils.copyProperties(deviceExt,d);
			String provinceName = regionService.getRegionName(d.getProvince());
			String cityName = regionService.getRegionName(d.getCity());
			String distributeName = regionService.getRegionName(d.getDistribute());
			deviceExt.setProvinceName(provinceName);
			deviceExt.setCityName(cityName);
			deviceExt.setDistributeName(distributeName);
			listExt.add(deviceExt);
		}
		page.setList(listExt);
		r.put("communicationBasePath", communicationBasePath);
		
		return r.put("page", page);
	}

	@ApiOperation(value = "查询单个设备信息")
	@GetMapping("/info/{deviceId}")
	public R info(@PathVariable("deviceId") Integer deviceId ){
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<DeviceEntity>();
		qw.eq("device_id", deviceId);
		DeviceEntity device = deviceService.getOne(qw);
		
		List<RegionEntity> provinceList = regionService.getProvinceList();
		List<RegionEntity> cityList = regionService.getRegionList(device.getProvince());
		List<RegionEntity> distributeList = regionService.getRegionList(device.getCity());
		
		R r = R.ok().put("device", device);
		r.put("provinceList", provinceList);
		r.put("cityList", cityList);
		r.put("distributeList", distributeList);
		return r;
	}
	
	/**
	 * 保存设备信息
	 */
	@PostMapping("/saveDevice")
	public R saveDevice(@RequestBody DeviceEntity device){
		device.setRegisterTime(new Date());
		device.setCreateTime(new Date());
		boolean b = deviceService.save(device);
		return b?R.ok("success"):R.ok("error");
	}
	
	/**
	 * 更新设备信息
	 */
	@PostMapping("/updateDevice")
	public R updateDevice(@RequestBody DeviceEntity device){
		boolean b = deviceService.saveOrUpdate(device);
		return b?R.ok("success"):R.ok("error");
	}
	
	/**
	 *删除货道
	 */
	@PostMapping("/delete")
	public R delete(@RequestBody List<Integer> idList ){
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<>();
		qw.in("device_id", idList);
		boolean b = deviceService.remove(qw);
		return b?R.ok("success"):R.ok("error");
	}
	
	/**
	 *设备坐标(小程序上的地图显示)
	 */
	@ApiOperation(value = "查询设备坐标,小程序上的地图显示附近的设备")
	@PostMapping("/deviceCoordinateList")
	public R deviceCoordinateList(@RequestBody Coordinate c){
		List<DeviceExt> deviceList = deviceService.queryDeviceListByCoordinateList(c.getLat(),c.getLng());
		return R.ok().put("deviceList", deviceList);
	}

	/**
	 *设备坐标(小程序上的地图显示)
	 */
	@ApiOperation(value = "查询设备坐标,小程序上的地图显示全部设备")
	@PostMapping("/deviceCoordinateAll")
	public R deviceCoordinateAll(@RequestBody Coordinate c){
		List<DeviceExt> deviceList = deviceService.queryDeviceAll(c);
		deviceList.sort(Comparator.comparingDouble(DeviceExt::getDistance)); //升序
		return R.ok().put("deviceList", deviceList);
	}

	/**
	 *设备坐标(小程序上的地图显示)
	 */
	@ApiOperation(value = "后台管理：设备地图")
	@GetMapping("/mapInit")
	@RequiresPermissions("device:map")
	public R mapInit(@RequestParam Map<String, Object> params){
		QueryWrapper<DeviceEntity> qw = new QueryWrapper<>();
		qw.eq("province", 440000);
		qw.eq("city", 440300);
		List<DeviceEntity> deviceList = deviceService.list();
		List<RegionEntity> provinceList = regionService.getProvinceList();
		R r = R.ok().put("provinceList", provinceList);
		r.put("deviceList", deviceList);
		return r;
	}
	
	/**
	 *设备坐标(小程序上的地图显示)
	 */
	@ApiOperation(value = "后台管理：设备地图")
	@GetMapping("/deviceForMap")
	@RequiresPermissions("device:map")
	public R deviceForMap(@RequestParam Map<String, Object> params){
		List<DeviceEntity> deviceList = deviceService.queryDeviceListForMap(params);
		R r = R.ok().put("deviceList", deviceList);
		return r;
	}
	
	/**
	 *设备坐标(设备坐标)
	 */
	@ApiOperation(value = "设备主动进行网络定位")
	@PostMapping("/positioning")
	public R positioning(@RequestBody Coordinate c){
		GPS gps = GPSConverterUtils.bd09_To_Gcj02(c.getLat(), c.getLng());
		UpdateWrapper<DeviceEntity> uw = new UpdateWrapper<>();
		uw.eq("device_code", c.getDeviceCode());
		uw.set("lng", gps.getLon());
		uw.set("lat", gps.getLat());
		deviceService.update(uw);
		return R.ok();
	}
	
	/**
	 *货道管理中的设备下拉框
	 */
	@PostMapping("/queryByRegion")
	public R queryByRegion(@RequestBody DeviceEntity device){
		List<DeviceEntity> deviceList = deviceService.queryByRegion(device);
		return R.ok().put("deviceList", deviceList);
	}
	
	/**
	 *设备离在线状态切换
	 */
	@PostMapping("/onlineSwitch")
	public String onlineSwitch(@RequestBody DeviceEntity device){
		log.info("接收到设备离线消息：{}", device.toString());
		boolean b = deviceService.onlineSwitch(device);
		return b?"success":"error";
	}
	
	/**
	 *保存设备密码
	 */
	@PostMapping("/savePass")
	public String savePass(@RequestBody PasswordVo param){
		boolean b = deviceService.savePass(param);
		return b?"success":"error";
	}
	
}

