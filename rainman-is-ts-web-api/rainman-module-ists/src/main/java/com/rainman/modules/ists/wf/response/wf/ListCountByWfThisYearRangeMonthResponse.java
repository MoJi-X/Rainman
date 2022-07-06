package com.rainman.modules.ists.wf.response.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("查询：总数：今年范围各月总数：响应参数")
@Data
@Accessors(chain = true)
public class ListCountByWfThisYearRangeMonthResponse {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "值", example = "project_rank")
    private Object value;
}
