package com.yunjian.modules.automat.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yunjian.common.utils.R;
import com.yunjian.modules.automat.entity.DeviceMemberEntity;
import com.yunjian.modules.automat.entity.DeviceWarnEntity;
import com.yunjian.modules.automat.service.DeviceMemberService;
import com.yunjian.modules.automat.service.DeviceWarnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yunjian.modules.sys.controller.AbstractController;

/**
 * <p>
 * 预警设备与会员的关系 前端控制器
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-18
 */
@RestController
@RequestMapping("/automat/deviceMember")
public class DeviceMemberController extends AbstractController {

    @Autowired
    private DeviceMemberService deviceMemberService;
    
    @Autowired
    private DeviceWarnService deviceWarnService;

    @PostMapping("/save")
    public R save(@RequestBody  DeviceMemberEntity dm){
        deviceMemberService.save(dm);
        
        UpdateWrapper<DeviceWarnEntity> uw = new UpdateWrapper<>();
        uw.eq("device_code", dm.getDeviceCode());
        uw.set("has_deliver_people", 1);
        deviceWarnService.update(uw);
        
        return R.ok();
    }

}

