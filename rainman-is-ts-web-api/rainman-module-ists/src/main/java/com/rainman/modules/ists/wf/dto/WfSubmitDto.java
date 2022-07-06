package com.rainman.modules.ists.wf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("提交:流程")
@Data
@Accessors(chain = true)
public class WfSubmitDto {
    /**
     * 流程任务标识
     */
    @ApiModelProperty(value = "流程任务标识")
    String taskId;

    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    String message;

    /**
     * 描述
     */
    @ApiModelProperty(value = "批准：同意（true）、不同意(false)", example = "true")
    private String approve;

    /**
     * 流程下一个节点处理人编号集合
     */
    @ApiModelProperty(value = "流程下一个节点处理人编号集合，逗号分隔", example = "1,2,3")
    private String wfNextNodeCandidateUsers;

    /**
     * 基础数据-项目标识
     */
    @ApiModelProperty(value = "基础数据-项目标识")
    private java.lang.String bdProjectId;
}
