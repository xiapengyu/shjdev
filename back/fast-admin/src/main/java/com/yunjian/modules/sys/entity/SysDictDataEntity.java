

package com.yunjian.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统配置信息
 * 
 * @author chenshun
 *
 * @date 2016年12月4日 下午6:43:36
 */
@Data
@TableName("sys_dict_data")
public class SysDictDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long dictCode;
	@NotBlank(message="标签不能为空")
	private String dictLabel;
	@NotBlank(message="值不能为空")
	private String dictValue;
	@NotBlank(message="类型不能为空")
	private String dictType;
	private Integer dictSort;
	private String remark;
}
