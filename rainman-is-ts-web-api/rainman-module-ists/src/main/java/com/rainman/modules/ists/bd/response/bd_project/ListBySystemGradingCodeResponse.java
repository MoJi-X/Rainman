package com.rainman.modules.ists.bd.response.bd_project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询：列表：系统定级总数：响应参数")
@Data
@Accessors(chain = true)
public class ListBySystemGradingCodeResponse {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String text;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String code;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "总数")
    private Long count;
}
