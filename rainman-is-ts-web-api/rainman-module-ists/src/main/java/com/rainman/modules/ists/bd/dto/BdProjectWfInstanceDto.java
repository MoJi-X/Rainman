package com.rainman.modules.ists.bd.dto;

import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 基础数据-项目与流程实例
 * @Author: yaohaitao
 * @Date: 2021-11-27
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础数据-项目与流程实例对象", description = "基础数据-项目与流程实例")
public class BdProjectWfInstanceDto extends BdProjectWfInstance {
    /**
     * 基础数据-项目
     */
    @ApiModelProperty(value = "基础数据-项目")
    BdProjectDto bdProject;

    /**
     * 流程-流程实例
     */
    @ApiModelProperty(value = "流程-流程实例")
    WfInstanceDto wfInstance;

    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "登录用户名，后端自动给值，前端不用传值")
    String loginUsername;

    /**
     * 页号
     */
    @ApiModelProperty(value = "页号", example = "1")
    Integer pageNo = 1;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小", example = "10")
    Integer pageSize = 10;
}
