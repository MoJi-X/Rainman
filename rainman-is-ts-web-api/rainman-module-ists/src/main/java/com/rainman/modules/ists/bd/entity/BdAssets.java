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
 * @Description: bd_assets
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Data
@TableName("bd_assets")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_assets对象", description="bd_assets")
public class BdAssets implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-资产标识*/
	@Excel(name = "基础数据-资产标识", width = 15)
    @ApiModelProperty(value = "基础数据-资产标识")
    @TableId(type = IdType.ASSIGN_ID)
    private java.lang.String bdAssetsId;
	/**设置类型编号*/
	@Excel(name = "设备类型编号", width = 15)
    @ApiModelProperty(value = "设置类型编号")
    private java.lang.String equipmentTypeCode;
	/**设置类型名称*/
	@Excel(name = "设置类型名称", width = 15)
    @ApiModelProperty(value = "设置类型名称")
    private java.lang.String equipmentTypeName;
	/**设备ip*/
	@Excel(name = "设备ip", width = 15)
    @ApiModelProperty(value = "设备ip")
    private java.lang.String equipmentIp;
	/**设备端口*/
	@Excel(name = "设备端口", width = 15)
    @ApiModelProperty(value = "设备端口")
    private java.lang.String equipmentPort;
	/**部署机房*/
	@Excel(name = "部署机房", width = 15)
    @ApiModelProperty(value = "部署机房")
    private java.lang.String deployComputerRoom;
	/**运维负责人名称*/
	@Excel(name = "运维负责人名称", width = 15)
    @ApiModelProperty(value = "运维负责人名称")
    private java.lang.String omPrincipalName;
	/**运维负责人编号*/
	@Excel(name = "运维负责人编号", width = 15)
    @ApiModelProperty(value = "运维负责人编号")
    private java.lang.String omPrincipalCode;
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
