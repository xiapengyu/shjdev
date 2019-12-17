package com.yunjian.modules.automat.controller;


import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeliverDetailEntity;
import com.yunjian.modules.automat.entity.DeliverRecordEntity;
import com.yunjian.modules.automat.service.DeliverDetailService;
import com.yunjian.modules.automat.service.DeliverRecordService;
import com.yunjian.modules.automat.vo.DeliverRecordExt;
import com.yunjian.modules.automat.vo.DeliveryParamVo;
import com.yunjian.modules.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Api(tags = "配送记录相关接口")
@RestController
@RequestMapping("/automat/deliverRecord")
public class DeliverRecordController extends AbstractController {
	@Autowired
	private DeliverRecordService deliverRecordService;
	@Autowired
	private DeliverDetailService deliverDetailService;
	
	@GetMapping("/list")
	@RequiresPermissions("deliverrecord:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = deliverRecordService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "【配送记录】我的")
	@RequestMapping(value="/client/deliveryList",method = RequestMethod.POST)
	public List<DeliverRecordExt> findByPhoneNo(@RequestBody DeliveryParamVo params) {
		return deliverRecordService.findByPhoneNo(params);
	}
	
	@GetMapping("/queryDeliverDetails")
	public R queryDeliverDetails(@RequestParam Map<String, Object> params){
		String recordId = params.get("recordId").toString();
		QueryWrapper<DeliverDetailEntity> query = new QueryWrapper<DeliverDetailEntity>();
		query.eq("record_id", Integer.parseInt(recordId));
		List<DeliverDetailEntity> resultList = deliverDetailService.list(query);
		return R.ok().put("result", resultList);
	}
	
}

