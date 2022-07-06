package com.rainman.modules.ists.wf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询:列表:响应参数")
@Data
@Accessors(chain = true)
public class WfCommentDto {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    String name;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    String code;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 最后受理人编号
     */
    @ApiModelProperty(value = "最后受理人编号")
    String lastAssigneeCode;

    /**
     * 最后受理人名称
     */
    @ApiModelProperty(value = "最后受理人名称")
    String lastAssigneeName;

    /**
     * 最后处理时间
     */
    @ApiModelProperty(value = "最后处理时间")
    String lastHandleTimeString;


    @ApiModelProperty(value = "消息")
    String message;
}
