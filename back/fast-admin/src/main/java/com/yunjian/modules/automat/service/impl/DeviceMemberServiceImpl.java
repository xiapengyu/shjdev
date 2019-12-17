package com.yunjian.modules.automat.service.impl;

import com.yunjian.modules.automat.entity.DeviceMemberEntity;
import com.yunjian.modules.automat.dao.DeviceMemberMapper;
import com.yunjian.modules.automat.service.DeviceMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预警设备与会员的关系 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Service("deviceMemberService")
public class DeviceMemberServiceImpl extends ServiceImpl<DeviceMemberMapper, DeviceMemberEntity> implements DeviceMemberService {

}
