

package com.yunjian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.modules.sys.entity.SysDictTypeEntity;
import com.yunjian.common.utils.PageUtils;

import java.util.Map;

/**
 * 系统字典信息
 * @author xiexiangrui
 */
public interface SysDictTypeService extends IService<SysDictTypeEntity> {

	public PageUtils queryPage(Map<String, Object> params);
}
