package com.yunjian.modules.automat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.dao.FeedbackMapper;
import com.yunjian.modules.automat.entity.FeedbackEntity;
import com.yunjian.modules.automat.entity.FeedbackImgEntity;
import com.yunjian.modules.automat.service.FeedbackImgService;
import com.yunjian.modules.automat.service.FeedbackService;
import com.yunjian.modules.automat.vo.FeedBackDto;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-07-12
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, FeedbackEntity> implements FeedbackService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	FeedbackMapper feedbackMapper;
	@Autowired
	FeedbackImgService feedbackImgService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String phoneNo = (String)params.get("phone");
		String startTime = (String) params.get("startTime");
		String endTime = (String) params.get("endTime");
		IPage<FeedbackEntity> page = this.page(
				new Query<FeedbackEntity>().getPage(params),
				new QueryWrapper<FeedbackEntity>()
					.like(StringUtils.isNotBlank(phoneNo),"phone", phoneNo)
					.ge(StringUtils.isNotBlank(startTime), "create_time", startTime)
					.le(StringUtils.isNotBlank(endTime), "create_time", endTime)
					.orderByDesc("create_time")
			);
			return new PageUtils(page);
	}
	
	@Override
	public PageUtils queryUserFeedBack(Map<String, Object> params) {
		String phoneNo = params.get("phone").toString();
		String limit = params.get("limit").toString();
		String pages = params.get("page").toString();
		params.put("limit", limit);
		params.put("page", pages);
		IPage<FeedbackEntity> page = this.page(
				new Query<FeedbackEntity>().getPage(params),
				new QueryWrapper<FeedbackEntity>().eq("phone", phoneNo)
				.orderByDesc("create_time")
			);
			return new PageUtils(page);
	}

	@Override
	@Transactional
	public R saveFeedBack(FeedBackDto dto) {
		try {
			FeedbackEntity entity = new FeedbackEntity();
			BeanUtils.copyProperties(dto, entity);
			entity.setStatus(1);
			feedbackMapper.saveFeedBack(entity);
			List<String> imgList = dto.getImageList();
			if(imgList != null && !imgList.isEmpty()) {
				List<FeedbackImgEntity> list = new ArrayList<FeedbackImgEntity>();
				for(String path : imgList) {
					FeedbackImgEntity image = new FeedbackImgEntity();
					image.setFeedbackId(entity.getFeedbackId());
					image.setImgPath(path);
					list.add(image);
				}
				if(list.size() > 0) {
					feedbackImgService.saveBatch(list);
				}
			}
		} catch (BeansException e) {
			logger.error("图片保存失败>>>[{}]", e);
			return R.error("图片保存失败");
		}
		return R.ok();
	}

}
