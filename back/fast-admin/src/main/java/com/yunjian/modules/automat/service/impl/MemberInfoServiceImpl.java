package com.yunjian.modules.automat.service.impl;

import com.yunjian.modules.automat.entity.MemberInfoEntity;
import com.yunjian.modules.automat.dao.MemberInfoMapper;
import com.yunjian.modules.automat.service.MemberInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员信息 服务实现类
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@Service("memberInfoService")
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfoEntity> implements MemberInfoService {

}
