package com.yunjian.modules.automat.controller;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.NoticeEntity;
import com.yunjian.modules.automat.service.NoticeService;
import com.yunjian.modules.sys.controller.AbstractController;
import com.yunjian.modules.sys.entity.SysUserEntity;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-08
 */
@RestController
@RequestMapping("/automat/notice")
public class NoticeController extends AbstractController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 所有公告列表
	 */
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = noticeService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@ApiOperation(value = "保存公告")
	@PostMapping("/save")
	public R save(@RequestBody NoticeEntity data){
		logger.info("保存公告>>>[{}]", data);
		data.setCreateTime(new Date());
		data.setStatus(1);
		SysUserEntity user = getUser();
		data.setCreateUser(user.getUsername());
		noticeService.save(data);
		return R.ok();
	}
	
	/**
	 * 修改公告
	 */
	@ApiOperation(value = "修改公告")
	@PostMapping("/update")
	public R update(@RequestBody NoticeEntity data){
		QueryWrapper<NoticeEntity> query = new QueryWrapper<NoticeEntity>();
		query.eq("notice_id", data.getNoticeId());
		NoticeEntity entity = noticeService.getOne(query);
		data.setCreateTime(entity.getCreateTime());
		SysUserEntity user = getUser();
		data.setCreateUser(user.getUsername());
		noticeService.saveOrUpdate(data);
		return R.ok();
	}
	
	/**
	 * 广告信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		QueryWrapper<NoticeEntity> query = new QueryWrapper<NoticeEntity>();
		query.eq("notice_id", id);
		NoticeEntity data = noticeService.getOne(query);
		return R.ok().put("ad", data);
	}
	
	/**
	 * app查询公告
	 * @return
	 */
	@PostMapping("/noticeList")
	public R queryNotice(){
		QueryWrapper<NoticeEntity> query = new QueryWrapper<NoticeEntity>();
		query.orderByDesc("create_time");
		query.eq("status", 1);
		List<NoticeEntity> list = noticeService.list(query);
		return R.ok().put("noticeList", list);
	}
}

