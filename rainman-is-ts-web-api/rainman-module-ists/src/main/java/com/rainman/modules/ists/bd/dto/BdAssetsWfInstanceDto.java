package com.rainman.modules.ists.bd.dto;

import com.rainman.modules.ists.bd.entity.BdAssets;
import com.rainman.modules.ists.bd.entity.BdAssetsWfInstance;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/2 17:10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础数据-资产与流程实例对象", description = "基础数据-资产与流程实例对象")
public class BdAssetsWfInstanceDto extends BdAssetsWfInstance {

    @ApiModelProperty(value = "基础数据-资产")
    private BdAssets bdAssets;

}
