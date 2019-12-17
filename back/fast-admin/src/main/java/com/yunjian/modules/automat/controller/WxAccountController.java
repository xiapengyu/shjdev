package com.yunjian.modules.automat.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.sys.controller.AbstractController;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@RestController
@RequestMapping("/automat/account")
public class WxAccountController extends AbstractController {
	@Autowired
	private WxAccountService wxAccountService;
	
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = wxAccountService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@GetMapping("/listAll")
	public R listAll(@RequestParam Map<String, Object> params){
		PageUtils page = wxAccountService.queryMgmtPage(params);
		return R.ok().put("page", page);
	}
}

