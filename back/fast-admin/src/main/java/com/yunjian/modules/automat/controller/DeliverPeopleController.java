package com.yunjian.modules.automat.controller;


import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.WxAccountEntity;
import com.yunjian.modules.automat.service.WxAccountService;
import com.yunjian.modules.sys.controller.AbstractController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@RestController
@RequestMapping("/automat/deliverPeople")
public class DeliverPeopleController extends AbstractController {
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@GetMapping("/list")
	@RequiresPermissions("deliverpeople:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = wxAccountService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@PostMapping("/updateType")
	public R updateType(@RequestBody Map<String, Object> params){
		int id = Integer.parseInt(params.get("id").toString());
		int type = Integer.parseInt(params.get("type").toString());
		QueryWrapper<WxAccountEntity> query = new QueryWrapper<WxAccountEntity>();
		query.eq("id", id);
		WxAccountEntity entity = new WxAccountEntity();
		entity.setType(type);
		entity.setDonate(null);
		entity.setAmount(null);
		wxAccountService.update(entity, query);
		return R.ok();
	}
}

