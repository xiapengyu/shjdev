package com.yunjian.modules.automat.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.automat.dao.NoticeMapper;
import com.yunjian.modules.automat.entity.NoticeEntity;
import com.yunjian.modules.automat.service.NoticeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-08
 */
@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("endTime");
        IPage<NoticeEntity> page = this.page(
                new Query<NoticeEntity>().getPage(params),
                new QueryWrapper<NoticeEntity>()
                .ge(StringUtils.isNotBlank(startTime),"create_time", startTime)
				.le(StringUtils.isNotBlank(endTime),"create_time", endTime)
				.orderByDesc("create_time")
        );
        return new PageUtils(page);
	}

}
