package com.rainman.modules.ists.bd.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rainman.modules.ists.bd.entity.BdProject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

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
@ApiModel(value = "bd_project对象", description = "bd_project")
public class BdProjectDto extends BdProject {
    /**
     * 项目立项时间
     */
    @ApiModelProperty(value = "项目立项时间")
    private String projectInitiationTimeString;
    /**
     * 系统定级名称
     */
    @Excel(name = "系统定级名称", width = 15)
    @ApiModelProperty(value = "系统定级名称")
    private String systemGradingName;
    /**
     * 系统类型名称
     */
    @Excel(name = "系统类型名称", width = 15)
    @ApiModelProperty(value = "系统类型名称")
    private String systemTypeName;
    /**
     * 项目负责人名称
     */
    @Excel(name = "项目负责人名称", width = 15)
    @ApiModelProperty(value = "项目负责人名称")
    private String projectLeaderName;
    /**
     * 项目负责人名称
     */
    @Excel(name = "项目部门名称", width = 15)
    @ApiModelProperty(value = "项目部门名称")
    private String projectDepartmentName;
    /**
     * 运维部门名称
     */
    @Excel(name = "运维部门名称", width = 15)
    @ApiModelProperty(value = "运维部门名称")
    private String omDepartmentName;
    /**
     * 运维负责人名称
     */
    @Excel(name = "运维负责人名称", width = 15)
    @ApiModelProperty(value = "运维负责人名称")
    private String omPrincipalName;
    /**
     * 开支类型名称
     */
    @Excel(name = "开支类型名称", width = 15)
    @ApiModelProperty(value = "开支类型名称")
    private String expenditureTypeName;
    /**
     * 服务开放范围名称
     */
    @Excel(name = "服务开放范围名称", width = 15)
    @ApiModelProperty(value = "服务开放范围名称")
    private String serviceOpeningScopeName;
    /**
     * 数据状态名称
     */
    @Excel(name = "数据状态名称", width = 15)
    @ApiModelProperty(value = "数据状态名称")
    private String dataStateName;
    /**
     * 创建人名称
     */
    @Excel(name = "创建人名称", width = 15)
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createdDateString;
    /**
     * 最后修改人名称
     */
    @Excel(name = "最后修改人名称", width = 15)
    @ApiModelProperty(value = "最后修改人名称")
    private String lastModifiedByName;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private String lastModifiedDateString;

    /**
     * 过程文件
     */
    @ApiModelProperty(value = "过程文件")
    private List<BdFileDto> bdFileList;

    /**
     * 页号
     */
    @ApiModelProperty(value = "页号", example = "1")
    Integer pageNo = 1;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小", example = "10")
    Integer pageSize = 10;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小", example = "10")
    List<String> inWfCodes;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "系统定级:project_rank；立项审批:project_check；特需审批:ineed_check；建设入网:network_access；安全验收:accept；变更报备:alter_report；安全运维:operation；风险评估:risk_assessment；处置备查:disposal；安全退网:network_exit", example = "project_rank")
    private String wfCode;

    /**
     * 流程节点编号
     */
    @ApiModelProperty(value = "流程节点编号")
    private String wfNodeCode;

    /**
     * 流程节点名称
     */
    @ApiModelProperty(value = "流程节点名称")
    private String wfNodeName;

    /**
     * 流程-实例标识
     */
    @ApiModelProperty(value = "流程-实例标识")
    private String wfInstanceId;

    /**
     * 系统定级流程-实例标识
     */
    @ApiModelProperty(value = "系统定级流程-实例标识")
    private String projectRankWfInstanceId;

    /**
     * 立项审批流程-实例标识
     */
    @ApiModelProperty(value = "立项审批流程-实例标识")
    private String projectCheckWfInstanceId;

    /**
     * 建设入网流程-实例标识
     */
    @ApiModelProperty(value = "建设入网流程-实例标识")
    private String networkAccessWfInstanceId;

    /**
     * 安全验收流程-实例标识
     */
    @ApiModelProperty(value = "安全验收流程-实例标识")
    private String acceptWfInstanceId;

    /**
     * 变更报备流程-实例标识
     */
    @ApiModelProperty(value = "变更报备流程-实例标识")
    private String alterReportWfInstanceId;

    /**
     * 安全运维流程-实例标识
     */
    @ApiModelProperty(value = "安全运维流程-实例标识")
    private String operationWfInstanceId;

    /**
     * 风险评估流程-实例标识
     */
    @ApiModelProperty(value = "风险评估流程-实例标识")
    private String riskAssessmentWfInstanceId;

    /**
     * 处置备查流程-实例标识
     */
    @ApiModelProperty(value = "处置备查流程-实例标识")
    private String disposalWfInstanceId;

    /**
     * 安全退网流程-实例标识
     */
    @ApiModelProperty(value = "安全退网流程-实例标识")
    private String networkExitWfInstanceId;

    /**
     * 系统定级流程状态名称
     */
    @ApiModelProperty(value = "系统定级流程状态名称")
    private String projectRankStateName;

    /**
     * 立项审批流程状态名称
     */
    @ApiModelProperty(value = "立项审批流程状态名称")
    private String projectCheckStateName;

    /**
     * 建设入网流程状态名称
     */
    @ApiModelProperty(value = "建设入网流程状态名称")
    private String networkAccessStateName;

    /**
     * 安全验收流程状态名称
     */
    @ApiModelProperty(value = "安全验收流程状态名称")
    private String acceptStateName;

    /**
     * 变更报备流程状态名称
     */
    @ApiModelProperty(value = "变更报备流程状态名称")
    private String alterReportStateName;

    /**
     * 安全运维流程状态名称
     */
    @ApiModelProperty(value = "安全运维流程状态名称")
    private String operationStateName;

    /**
     * 风险评估流程状态名称
     */
    @ApiModelProperty(value = "风险评估流程状态名称")
    private String riskAssessmentStateName;

    /**
     * 处置备查流程状态名称
     */
    @ApiModelProperty(value = "处置备查流程状态名称")
    private String disposalStateName;

    /**
     * 安全退网流程状态名称
     */
    @ApiModelProperty(value = "安全退网流程状态名称")
    private String networkExitStateName;

    /**
     * 流程实例状态编号
     */
    @ApiModelProperty(value = "流程实例状态编号")
    private String wfInstanceStateCode;

}
