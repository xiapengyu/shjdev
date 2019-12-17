package com.yunjian.modules.automat.controller;


import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.common.annotation.SysLog;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.common.validator.ValidatorUtils;
import com.yunjian.modules.automat.entity.QrCodeEntity;
import com.yunjian.modules.automat.service.OrderService;
import com.yunjian.modules.automat.service.QrCodeService;

/**
 * 工单管理
 */
@RestController
@RequestMapping("/automat/qrCode")
public class QrCodeController {
	@Autowired
	private QrCodeService qrCodeService;
	@Autowired
	private OrderService orderService;

	/**
	 * 所有二维码列表
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = orderService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	/**
	 * 下载二维码
	 */
	@GetMapping("/download")
	public R downLoadQrcode(@PathVariable("url") String url){
		return null;
	}

	/**
	 * 工单信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		QrCodeEntity dictData = qrCodeService.getById(id);
		return R.ok().put("dictData", dictData);
	}


	/**
	 * 保存配置
	 */
	@SysLog("保存工单")
	@PostMapping("/save")
	public R save(@RequestBody QrCodeEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		qrCodeService.saveOrUpdate(dictData);
		return R.ok();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改工单")
	@PostMapping("/update")
	@RequiresPermissions("automat:qrcode:update")
	public R update(@RequestBody QrCodeEntity dictData){
		ValidatorUtils.validateEntity(dictData);
		qrCodeService.saveOrUpdate(dictData);
		return R.ok();
	}


	/**
	 * 删除配置
	 */
	@SysLog("删除工单")
	@PostMapping("/delete")
	@RequiresPermissions("automat:qrcode:delete")
	public R delete(@RequestBody Long[] ids){
		qrCodeService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}
	
}
