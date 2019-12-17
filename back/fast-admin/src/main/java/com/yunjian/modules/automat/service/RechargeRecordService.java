package com.yunjian.modules.automat.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.RechargeRecordEntity;
import com.yunjian.modules.automat.vo.RechargeReqDto;

/**
 * <p>
 * 充值记录 服务类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-04
 */
public interface RechargeRecordService extends IService<RechargeRecordEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRechargeRecord(RechargeRecordEntity rechargeRecordEntity);

	void updateRechargeRecord(RechargeRecordEntity rechargeRecordEntity);

	List<RechargeRecordEntity> findByPhoneNo(String phoneNo);
	
	R recharge(RechargeReqDto requestDto, HttpServletRequest request);
	
	Object weixinRechargeNotify(HttpServletRequest request, HttpServletResponse response);

}
