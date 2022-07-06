package com.rainman.modules.ists.wf.entity;

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
 * @Description: wf_instance
 * @Author: yao-hai-tao
 * @Date: 2021-11-25
 * @Version: V1.0
 */
@Data
@TableName("wf_instance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wf_instance对象", description="wf_instance")
public class WfInstance implements Serializable {
    private static final long serialVersionUID = 1L;

	/**流程-实例标识*/
	@Excel(name = "流程-实例标识", width = 15)
    @ApiModelProperty(value = "流程-实例标识")
    @TableId(type = IdType.ASSIGN_ID)
    private String wfInstanceId;
	/**基础数据-流水单编号*/
	@Excel(name = "基础数据-流水单编号", width = 15)
    @ApiModelProperty(value = "基础数据-流水单编号")
    private String bdFlowSheetCode;
	/**流程编号*/
	@Excel(name = "流程编号", width = 15)
    @ApiModelProperty(value = "系统定级:project_rank；立项审批:project_check；特需审批:ineed_check；建设入网:network_access；安全验收:accept；变更报备:alter_report；安全运维:operation；风险评估:risk_assessment；处置备查:disposal；安全退网:network_exit", example = "project_rank")
    private String wfCode;
	/**流程节点编号*/
	@Excel(name = "流程节点编号", width = 15)
    @ApiModelProperty(value = "流程节点编号")
    private String wfNodeCode;
    /**流程节点处理人编号集合*/
    @Excel(name = "流程节点处理人编号集合", width = 15)
    @ApiModelProperty(value = "流程节点处理人编号集合")
    private String wfNodeAssigneeCodes;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
    /**数据状态编号*/
    @Excel(name = "数据状态编号", width = 15)
    @ApiModelProperty(value = "数据状态编号")
    private java.lang.String dataStateCode;
	/**状态编号*/
	@Excel(name = "状态编号", width = 15)
    @ApiModelProperty(value = "状态编号")
    private String stateCode;
    /**
     * 过程标识(流程引擎中流程实例标识)
     */
    @Excel(name = "过程标识(流程引擎中流程实例标识)", width = 15)
    @ApiModelProperty(value = "过程标识(流程引擎中流程实例标识)")
    private String processId;
    /**
     * 发起时间
     */
    @Excel(name = "发起时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "发起时间")
    private Date initiationDate;
    /**
     * 申请时间
     */
    @Excel(name = "申请时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "申请时间")
    private Date applyTime;
    /**
     * 发起人编号
     */
    @Excel(name = "发起人编号", width = 15)
    @ApiModelProperty(value = "发起人编号")
    private String initiatorCode;
    /**
     * 发起人部门编号
     */
    @Excel(name = "发起人部门编号", width = 15)
    @ApiModelProperty(value = "发起人部门编号")
    private String initiatorDepartmentCode;
    /**
     * 创建人编号
     */
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
