

package com.yunjian.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 *
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
