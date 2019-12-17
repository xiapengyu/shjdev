

package com.yunjian.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.modules.sys.dao.SysDictTypeDao;
import com.yunjian.modules.sys.entity.SysDictTypeEntity;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.sys.service.SysDictTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictTypeEntity> implements SysDictTypeService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String dictName = (String)params.get("dictName");


		IPage<SysDictTypeEntity> page = this.page(
				new Query<SysDictTypeEntity>().getPage(params),
				new QueryWrapper<SysDictTypeEntity>()
						.like(StringUtils.isNotBlank(dictName),"dict_name", dictName)
						.eq("status", 1)
		);
		return new PageUtils(page);
	}
}
