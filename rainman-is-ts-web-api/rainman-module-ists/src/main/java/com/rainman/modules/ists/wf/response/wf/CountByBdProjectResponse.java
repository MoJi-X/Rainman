package com.rainman.modules.ists.wf.response.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询：总数：三同步整体概况(同步规划、同步建设、同步运行)：响应参数")
@Data
@Accessors(chain = true)
public class CountByBdProjectResponse {
    /**
     * 项目总数
     */
    @ApiModelProperty(value = "项目总数")
    private Long allCount;

    /**
     * 走了同步规则流程的项目总数
     */
    @ApiModelProperty(value = "走了同步规则流程的项目总数")
    private Long planCount;

    /**
     * 走了同步建设流程的项目总数
     */
    @ApiModelProperty(value = "走了同步建设流程的项目总数")
    private Long buildCount;

    /**
     * 走了同步运行流程的项目总数
     */
    @ApiModelProperty(value = "走了同步运行流程的项目总数")
    private Long runtimeCount;
}
