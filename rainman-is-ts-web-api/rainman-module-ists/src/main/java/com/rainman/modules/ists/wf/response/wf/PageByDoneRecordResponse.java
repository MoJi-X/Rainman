package com.rainman.modules.ists.wf.response.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询：分页：响应参数")
@Data
@Accessors(chain = true)
public class PageByDoneRecordResponse {
    /**
     * 流程-实例标识
     */
    @ApiModelProperty(value = "流程-实例标识")
    private String wfInstanceId;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "系统定级:project_rank；立项审批:project_check；特需审批:ineed_check；建设入网:network_access；安全验收:accept；变更报备:alter_report；安全运维:operation；风险评估:risk_assessment；处置备查:disposal；安全退网:network_exit", example = "project_rank")
    private String wfCode;

    @ApiModelProperty(value = "流程名稱")
    private String wfName;

    /**
     * 流程节点编号
     */
    @ApiModelProperty(value = "流程节点编号")
    private String wfNodeCode;

    @ApiModelProperty(value = "流程節點名稱")
    private String wfNodeName;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 状态编号
     */
    @ApiModelProperty(value = "状态编号")
    private String stateCode;

    /**
     * 状态编号
     */
    @ApiModelProperty(value = "状态编号")
    private String stateName;

    /**
     * 上一步处理结果
     */
    @ApiModelProperty(value = "上一步处理结果")
    private String previousStepMessage;

    /**
     * 上一步流程节点
     */
    @ApiModelProperty(value = "上一步流程节点")
    private String previousStepWfNodeName;

    /**
     * 上一步流程处理人
     */
    @ApiModelProperty(value = "上一步流程处理人")
    private String previousStepAssigneeName;

    /**
     * 上一步处理时间
     */
    @ApiModelProperty(value = "上一步处理时间")
    private String previousStepEndTimeString;
}
