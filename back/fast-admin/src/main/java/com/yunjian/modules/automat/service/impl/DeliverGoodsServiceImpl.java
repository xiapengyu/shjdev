package com.yunjian.modules.automat.service.impl;

import com.yunjian.modules.automat.entity.DeliverGoodsEntity;
import com.yunjian.modules.automat.dao.DeliverGoodsMapper;
import com.yunjian.modules.automat.service.DeliverGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配送记录与商品关系表 服务实现类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-02
 */
@Service("deliverGoodsService")
public class DeliverGoodsServiceImpl extends ServiceImpl<DeliverGoodsMapper, DeliverGoodsEntity> implements DeliverGoodsService {

}
