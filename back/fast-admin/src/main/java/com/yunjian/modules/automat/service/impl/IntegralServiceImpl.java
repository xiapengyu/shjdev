package com.yunjian.modules.automat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.IntegralMapper;
import com.yunjian.modules.automat.entity.IntegralEntity;
import com.yunjian.modules.automat.service.IntegralService;

/**
 * <p>
 * 积分信息 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-22
 */
@Service("integralService")
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, IntegralEntity> implements IntegralService {

	@Override
	public R getIntegralInfoList(String phoneNo) {
		QueryWrapper<IntegralEntity> qw = new QueryWrapper<IntegralEntity>();
		qw.eq("phone_no", phoneNo);
		qw.orderByDesc("create_time");
		List<IntegralEntity> list = list(qw);
		int score = 0;
		for(IntegralEntity ie : list) {
			score += ie.getScore();
		}
		return R.ok().put("integral", score).put("data", list);
	}

}
