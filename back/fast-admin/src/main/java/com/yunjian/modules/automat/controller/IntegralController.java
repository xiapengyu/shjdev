package com.yunjian.modules.automat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.service.IntegralService;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 积分信息 前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-06-22
 */
@Api(tags = "积分相关接口")
@RestController
@RequestMapping("/automat/integral")
public class IntegralController extends AbstractController {
	@Autowired
	private IntegralService integralService;
	
	@ApiOperation(value = "我的积分页信息")
	@ApiImplicitParams({
	    @ApiImplicitParam(name="phoneNo",value="手机号码", required=true, dataType="String"),
	})
	@GetMapping("/getIntegralInfoList")
	public R getIntegralInfoList(String phoneNo) {
		return integralService.getIntegralInfoList(phoneNo);
	}
}

