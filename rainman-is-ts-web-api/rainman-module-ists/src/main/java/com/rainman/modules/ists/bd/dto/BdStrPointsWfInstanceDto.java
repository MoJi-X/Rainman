package com.rainman.modules.ists.bd.dto;

import com.rainman.modules.ists.bd.entity.BdStrPointsWfInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/3 10:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础数据-风险与实例", description = "基础数据-风险与实例")
public class BdStrPointsWfInstanceDto extends BdStrPointsWfInstance {
    /**基础数据-风险点对象*/
    @ApiModelProperty(value = "基础数据-风险点对象")
    private BdStrPointsDto bdStrPointsDto;
}
