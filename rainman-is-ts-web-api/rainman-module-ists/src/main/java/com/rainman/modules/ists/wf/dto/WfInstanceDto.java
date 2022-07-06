package com.rainman.modules.ists.wf.dto;

import com.rainman.modules.ists.wf.entity.WfInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("导出:群消息链接:响应参数")
@Data
@Accessors(chain = true)
public class WfInstanceDto extends WfInstance {
    @ApiModelProperty(value = "发起人名稱")
    private String initiatorName;

    @ApiModelProperty(value = "发起人部门名稱")
    private String initiatorDepartmentName;

    @ApiModelProperty(value = "流程名稱")
    private String wfName;

    @ApiModelProperty(value = "状态名稱")
    private String stateName;

    @ApiModelProperty(value = "流程節點名稱")
    private String wfNodeName;

    @ApiModelProperty(value = "发起时间字符串")
    private String initiationDateString;

    /**
     * 流程任务标识
     */
    @ApiModelProperty(value = "流程任务标识")
    String taskId;
}
