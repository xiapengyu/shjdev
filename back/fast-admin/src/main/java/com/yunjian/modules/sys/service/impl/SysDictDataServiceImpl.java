

package com.yunjian.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunjian.modules.sys.dao.SysDictDataDao;
import com.yunjian.modules.sys.entity.SysDictDataEntity;
import com.yunjian.common.utils.PageUtils;
import com.yunjian.common.utils.Query;
import com.yunjian.modules.sys.service.SysDictDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sysDictDataService")
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictDataEntity> implements SysDictDataService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String dictType = (String)params.get("dictType");
        String dictLabel = (String)params.get("dictLabel");

        IPage<SysDictDataEntity> page = this.page(
                new Query<SysDictDataEntity>().getPage(params),
                new QueryWrapper<SysDictDataEntity>()
                        .eq(StringUtils.isNotBlank(dictType),"dict_type", dictType)
                        .like(StringUtils.isNotBlank(dictLabel),"dict_label", dictLabel)
        );
        return new PageUtils(page);
    }
}
