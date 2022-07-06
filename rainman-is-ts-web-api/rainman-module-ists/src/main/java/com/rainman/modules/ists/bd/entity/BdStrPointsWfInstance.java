package com.rainman.modules.ists.bd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: bd_str_points_wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bd_str_points_wf_instance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_str_points_wf_instance对象", description="bd_str_points_wf_instance")
public class BdStrPointsWfInstance implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-安全测试风险点与流程-实例标识*/
	@Excel(name = "基础数据-安全测试风险点与流程-实例标识", width = 15)
    @ApiModelProperty(value = "基础数据-安全测试风险点与流程-实例标识")
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String bdStrPointsWfInstanceId;
	/**流程-实例标识*/
	@Excel(name = "流程-实例标识", width = 15)
    @ApiModelProperty(value = "流程-实例标识")
    private java.lang.String wfInstanceId;
	/**基础数据-项目标识*/
	@Excel(name = "基础数据-项目标识", width = 15)
    @ApiModelProperty(value = "基础数据-项目标识")
    private java.lang.String bdProjectId;
	/**基础数据-安全测试风险点标识*/
	@Excel(name = "基础数据-安全测试风险点标识", width = 15)
    @ApiModelProperty(value = "基础数据-安全测试风险点标识")
    private java.lang.String bdStrPointsId;
}
