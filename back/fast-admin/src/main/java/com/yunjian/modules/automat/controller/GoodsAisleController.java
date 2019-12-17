package com.yunjian.modules.automat.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceEntity;
import com.yunjian.modules.automat.entity.GoodsAisleEntity;
import com.yunjian.modules.automat.entity.GoodsEntity;
import com.yunjian.modules.automat.entity.RegionEntity;
import com.yunjian.modules.automat.service.DeviceService;
import com.yunjian.modules.automat.service.GoodsAisleService;
import com.yunjian.modules.automat.service.GoodsService;
import com.yunjian.modules.automat.service.RegionService;
import com.yunjian.modules.automat.vo.AisleExt;
import com.yunjian.modules.sys.controller.AbstractController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 货道信息管理表 前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-01
 */
@Slf4j
@RestController
@RequestMapping("/automat/aisle")
public class GoodsAisleController extends AbstractController {

	@Autowired
	private GoodsAisleService goodsAisleService;

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private RegionService regionService;

	/**
	 * 所有设备列表
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@GetMapping("/list")
	@RequiresPermissions("aisle:list")
	public R list(@RequestParam Map<String, Object> params) throws IllegalAccessException, InvocationTargetException {
		PageUtils page = goodsAisleService.queryPage(params);
		R r = R.ok().put("page", page);
		return r;
	}

	@GetMapping("/info/{aisleId}")
	public R info(@PathVariable("aisleId") Integer aisleId) {
		QueryWrapper<GoodsEntity> goodsQW = new QueryWrapper<>();
		goodsQW.eq("status",1);
		List<GoodsEntity> goodsList = goodsService.list(goodsQW);
		R r = R.ok().put("goodsList", goodsList);
		
		DeviceEntity device = deviceService.queryByAisleId(aisleId);
		List<RegionEntity> provinceList = regionService.getProvinceList();
		List<RegionEntity> cityList = regionService.getRegionList(device.getProvince());
		List<RegionEntity> distributeList = regionService.getRegionList(device.getCity());
		List<DeviceEntity> deviceList = deviceService.queryByRegion(device);
		r.put("provinceList", provinceList);
		r.put("cityList", cityList);
		r.put("distributeList", distributeList);
		r.put("deviceList", deviceList);
		r.put("device", device);
		
		QueryWrapper<GoodsAisleEntity> qw = new QueryWrapper<>();
		qw.eq("aisle_id", aisleId);
		GoodsAisleEntity aisle = goodsAisleService.getOne(qw);
		r.put("aisle", aisle);
		return r;
	}

	@PostMapping("/saveAisle")
	public R saveAisle(@RequestBody GoodsAisleEntity aisle) {
		R r = goodsAisleService.saveAisle(aisle);
		return r;
	}

	/**
	 *删除设备
	 */
	@PostMapping("/delete")
	public R delete(@RequestBody List<Integer> idList ){
		QueryWrapper<GoodsAisleEntity> qw = new QueryWrapper<>();
		qw.in("aisle_id", idList);
		boolean b = goodsAisleService.remove(qw);
		return b?R.ok("success"):R.ok("error");
	}
	
	@PostMapping("/updateAilse")
	public R updateAisle(@RequestBody GoodsAisleEntity aisle) {
		UpdateWrapper<GoodsAisleEntity> uw = new UpdateWrapper<>();
		uw.eq("aisle_id", aisle.getAisleId());
		boolean b = goodsAisleService.update(aisle, uw);
		if(b) {
			try {
				goodsAisleService.checkStockout(aisle.getDeviceCode());	
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return R.error();
			}
		}
		return R.ok();
	}

	@RequestMapping(value = "/baseGoodsAndDevice", method = RequestMethod.GET)
	public R baseGoodsAndDevice() {
		QueryWrapper<GoodsEntity> goodsQW = new QueryWrapper<>();
		goodsQW.eq("status",1);
		List<GoodsEntity> goodsList = goodsService.list(goodsQW);
		R r = R.ok().put("goodsList", goodsList);
		
		List<RegionEntity> provinceList = regionService.getProvinceList();
		r.put("provinceList", provinceList);
		
		return r;
	}
	
	@RequestMapping(value = "/queryTaskAisle", method = RequestMethod.GET)
	public R queryTaskAisle(String phoneNo) {
		List<AisleExt> goodsAisleList = goodsAisleService.queryTaskAisle(phoneNo);
		R r = R.ok().put("goodsAisleList", goodsAisleList);
		return r;
	}

}
