package com.yunjian.modules.automat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunjian.modules.automat.entity.DeliverRecordEntity;
import com.yunjian.modules.automat.vo.DeliverRecordExt;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapengyu
 * @since 2019-06-16
 */
@Mapper
public interface DeliverRecordMapper extends BaseMapper<DeliverRecordEntity> {

	List<DeliverRecordExt> findByPhoneNo(@Param("phoneNo")String phoneNo, @Param("index")Integer index);

}
