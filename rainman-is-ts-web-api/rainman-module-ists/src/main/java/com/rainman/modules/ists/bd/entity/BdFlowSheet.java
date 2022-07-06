package com.rainman.modules.ists.bd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: bd_flow_sheet
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bd_flow_sheet")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_flow_sheet对象", description="bd_flow_sheet")
public class BdFlowSheet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-流水单标识*/
	@Excel(name = "基础数据-流水单标识", width = 15)
    @ApiModelProperty(value = "基础数据-流水单标识")
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String bdFlowSheetId;
	/**基础数据-流水单类型编号*/
	@Excel(name = "基础数据-流水单类型编号", width = 15)
    @ApiModelProperty(value = "基础数据-流水单类型编号")
    private java.lang.String bdFlowSheetTypeCode;
	/**基础数据-流水单编号*/
	@Excel(name = "基础数据-流水单编号", width = 15)
    @ApiModelProperty(value = "基础数据-流水单编号")
    private java.lang.String bdFlowSheetCode;
	/**状态编号*/
	@Excel(name = "状态编号", width = 15)
    @ApiModelProperty(value = "状态编号")
    private java.lang.String stateCode;
	/**创建人编号*/
	@Excel(name = "创建人编号", width = 15)
    @ApiModelProperty(value = "创建人编号")
    private java.lang.String createdByCode;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createdDate;
	/**最后修改人编号*/
	@Excel(name = "最后修改人编号", width = 15)
    @ApiModelProperty(value = "最后修改人编号")
    private java.lang.String lastModifiedByCode;
	/**最后修改时间*/
	@Excel(name = "最后修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最后修改时间")
    private java.util.Date lastModifiedDate;
}
