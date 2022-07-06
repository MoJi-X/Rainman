package com.rainman.modules.ists.wf.response.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询：列表：三同步部门开展情况：响应参数")
@Data
@Accessors(chain = true)
public class ListCountByOrgForWfResponse {
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String orgName;

    /**
     * 部门编号
     */
    @ApiModelProperty(value = "部门编号")
    private String orgCode;

    /**
     * 走了同步规则流程的流程总数
     */
    @ApiModelProperty(value = "走了同步规则流程的流程总数")
    private Long planCount;

    /**
     * 走了同步建设流程的流程总数
     */
    @ApiModelProperty(value = "走了同步建设流程的流程总数")
    private Long buildCount;

    /**
     * 走了同步运行流程的流程总数
     */
    @ApiModelProperty(value = "走了同步运行流程的流程总数")
    private Long runtimeCount;
}
