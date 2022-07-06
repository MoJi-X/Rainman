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
 * @Description: bd_str_points
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bd_str_points")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_str_points对象", description="bd_str_points")
public class BdStrPoints implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-安全测试风险点标识*/
	@Excel(name = "基础数据-安全测试风险点标识", width = 15)
    @ApiModelProperty(value = "基础数据-安全测试风险点标识")
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String bdStrPointsId;
	/**风险等级编号*/
	@Excel(name = "风险等级编号", width = 15)
    @ApiModelProperty(value = "风险等级编号")
    private java.lang.String riskLevelCode;
	/**风险整改开始时间*/
	@Excel(name = "风险整改开始时间", width = 15)
    @ApiModelProperty(value = "风险整改开始时间")
    private java.lang.String riskRectificationBeginDate;
	/**风险整改结束时间*/
	@Excel(name = "风险整改结束时间", width = 15)
    @ApiModelProperty(value = "风险整改结束时间")
    private java.lang.String riskRectificationEndDate;
	/**风险点*/
	@Excel(name = "风险点", width = 15)
    @ApiModelProperty(value = "风险点")
    private java.lang.String riskPoints;
	/**整改描述*/
	@Excel(name = "整改描述", width = 15)
    @ApiModelProperty(value = "整改描述")
    private java.lang.String rectificationDescription;
	/**整改建议*/
	@Excel(name = "整改建议", width = 15)
    @ApiModelProperty(value = "整改建议")
    private java.lang.String rectificationProposal;
	/**整改结果*/
	@Excel(name = "整改结果", width = 15)
    @ApiModelProperty(value = "整改结果")
    private java.lang.String rectificationResult;
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
