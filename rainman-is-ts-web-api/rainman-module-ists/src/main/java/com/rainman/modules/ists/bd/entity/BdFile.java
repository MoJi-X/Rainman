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
import java.util.Date;

/**
 * @Description: bd_file
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Data
@TableName("bd_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bd_file对象", description="bd_file")
public class BdFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**基础数据-文件标识*/
	@Excel(name = "基础数据-文件标识", width = 15)
    @ApiModelProperty(value = "基础数据-文件标识")
    @TableId(type = IdType.ASSIGN_ID)
    private String bdFileId;
	/**流程-实例标识*/
	@Excel(name = "流程-实例标识", width = 15)
    @ApiModelProperty(value = "流程-实例标识")
    private String wfInstanceId;
	/**表名*/
	@Excel(name = "表名", width = 15)
    @ApiModelProperty(value = "表名")
    private String tableName;
	/**表标识*/
	@Excel(name = "表标识", width = 15)
    @ApiModelProperty(value = "表标识")
    private String tableId;
	/**标记编号*/
	@Excel(name = "标记编号", width = 15)
    @ApiModelProperty(value = "标记编号")
    private String flagCode;
	/**存储类型*/
	@Excel(name = "存储类型", width = 15)
    @ApiModelProperty(value = "存储类型")
    private String storageType;
	/**相对路径*/
	@Excel(name = "相对路径", width = 15)
    @ApiModelProperty(value = "相对路径")
    private String relativePath;
	/**文件名*/
	@Excel(name = "文件名", width = 15)
    @ApiModelProperty(value = "文件名")
    private String filename;
	/**创建人编号*/
	@Excel(name = "创建人编号", width = 15)
    @ApiModelProperty(value = "创建人编号")
    private String createdByCode;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;
	/**最后修改人编号*/
	@Excel(name = "最后修改人编号", width = 15)
    @ApiModelProperty(value = "最后修改人编号")
    private String lastModifiedByCode;
	/**最后修改时间*/
	@Excel(name = "最后修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifiedDate;
}
