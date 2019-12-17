package com.yunjian.modules.automat.service.impl;

import com.yunjian.modules.automat.entity.OrderDetailEntity;
import com.yunjian.modules.automat.dao.OrderDetailMapper;
import com.yunjian.modules.automat.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetailEntity> implements OrderDetailService {

}
