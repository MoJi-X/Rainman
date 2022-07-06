package com.rainman.modules.ists.bd.dto;

import com.rainman.modules.ists.bd.entity.BdStrPoints;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/3 10:50
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础数据-风险点对象", description = "基础数据-风险点对象")
public class BdStrPointsDto extends BdStrPoints {
    /**风险等级*/
    @ApiModelProperty(value = "风险等级编号")
    String riskLevelName;
}
