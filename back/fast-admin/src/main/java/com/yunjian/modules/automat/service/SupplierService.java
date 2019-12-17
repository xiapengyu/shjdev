package com.yunjian.modules.automat.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.automat.entity.SupplierEntity;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author laizhiwen
 * @since 2019-08-16
 */
public interface SupplierService extends IService<SupplierEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
