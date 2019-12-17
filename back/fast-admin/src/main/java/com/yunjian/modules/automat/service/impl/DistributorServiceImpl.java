package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.DistributorMapper;
import com.yunjian.modules.automat.entity.DistributorEntity;
import com.yunjian.modules.automat.service.DistributorService;
import com.yunjian.modules.automat.util.StringUtil;

/**
 * <p>
 * 经销商信息 服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-16
 */
@Service("distributorService")
public class DistributorServiceImpl extends ServiceImpl<DistributorMapper, DistributorEntity>
		implements DistributorService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String name = StringUtil.object2String(params.get("name"));
		String contact = StringUtil.object2String(params.get("contact"));

		QueryWrapper<DistributorEntity> query = new QueryWrapper<DistributorEntity>();
		if (!StringUtils.isEmpty(name)) {
			query.like("name", name);
		}
		if (!StringUtils.isEmpty(contact)) {
			query.like("contact", contact);
		}
		query.orderByDesc("create_time");
		IPage<DistributorEntity> page = this.page(new Query<DistributorEntity>().getPage(params), query);
		return new PageUtils(page);
	}

}
