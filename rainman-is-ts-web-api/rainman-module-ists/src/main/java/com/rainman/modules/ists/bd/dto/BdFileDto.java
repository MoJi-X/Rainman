package com.rainman.modules.ists.bd.dto;

import com.rainman.modules.ists.bd.entity.BdFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 基础数据-文件
 * @Author: yao-hai-tao
 * @Date: 2021-11-25
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础数据-文件", description = "基础数据-文件")
public class BdFileDto extends BdFile {
    @ApiModelProperty(value = "标记名称")
    private String flagName;

    /**
     * 流程节点编号
     */
    @ApiModelProperty(value = "流程节点编号")
    private String wfNodeCode;
}
