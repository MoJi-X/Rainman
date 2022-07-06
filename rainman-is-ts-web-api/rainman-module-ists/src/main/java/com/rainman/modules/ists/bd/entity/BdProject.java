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
 * @Description: bd_project
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bd_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_project对象", description="bd_project")
public class BdProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-项目标识*/
	@Excel(name = "基础数据-项目标识", width = 15)
    @ApiModelProperty(value = "基础数据-项目标识")
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String bdProjectId;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
	/**系统定级编号*/
	@Excel(name = "系统定级编号", width = 15)
    @ApiModelProperty(value = "系统定级编号")
    private java.lang.String systemGradingCode;
	/**年度*/
	@Excel(name = "年度", width = 15)
    @ApiModelProperty(value = "年度")
    private java.lang.Integer year;
	/**项目立项时间*/
	@Excel(name = "项目立项时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目立项时间")
    private java.util.Date projectInitiationTime;
	/**系统类型编号*/
	@Excel(name = "系统类型编号", width = 15)
    @ApiModelProperty(value = "系统类型编号")
    private java.lang.String systemTypeCode;
	/**项目负责人编号*/
	@Excel(name = "项目负责人编号", width = 15)
    @ApiModelProperty(value = "项目负责人编号")
    private java.lang.String projectLeaderCode;
	/**运维部门编号*/
	@Excel(name = "运维部门编号", width = 15)
    @ApiModelProperty(value = "运维部门编号")
    private java.lang.String omDepartmentCode;
	/**运维负责人编号*/
	@Excel(name = "运维负责人编号", width = 15)
    @ApiModelProperty(value = "运维负责人编号")
    private java.lang.String omPrincipalCode;
	/**开支类型编号*/
	@Excel(name = "开支类型编号", width = 15)
    @ApiModelProperty(value = "开支类型编号")
    private java.lang.String expenditureTypeCode;
	/**服务开放范围编号*/
	@Excel(name = "服务开放范围编号", width = 15)
    @ApiModelProperty(value = "服务开放范围编号")
    private java.lang.String serviceOpeningScopeCode;
	/**数据状态编号*/
	@Excel(name = "数据状态编号", width = 15)
    @ApiModelProperty(value = "数据状态编号")
    private java.lang.String dataStateCode;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
    /**发起人编号*/
    @Excel(name = "发起人编号", width = 15)
    @ApiModelProperty(value = "发起人编号")
    private String initiatorCode;
    /**发起人部门编号*/
    @Excel(name = "发起人部门编号", width = 15)
    @ApiModelProperty(value = "发起人部门编号")
    private String initiatorDepartmentCode;
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
