package com.rainman.modules.ists.bd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.dto.BdProjectDto;
import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.response.bd_project.ListBySystemGradingCodeResponse;
import com.rainman.modules.ists.bd.service.IBdFileService;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import com.rainman.util.RunnableUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 基础数据-项目
 * @Author: yao-hai-tao
 * @Date: 2021-11-23
 * @Version: V1.0
 */
@Api(tags = "基础数据-项目")
@RestController
@RequestMapping("/ists.bd/bdProject")
@Slf4j
public class BdProjectController extends JeecgController<BdProject, IBdProjectService> {
    @Autowired
    private IBdProjectService bdProjectService;

    @Autowired
    private IBdFileService bdFileService;

    @Autowired
    private IWfInstanceService wfInstanceService;

    /**
     * 新增：单个
     *
     * @param bdProjectDto
     * @return
     */
    @AutoLog(value = "新增：单个")
    @ApiOperation(value = "新增：单个", notes = "新增：单个")
    @PostMapping(value = "/add")
    public Result<BdProjectDto> add(@RequestBody BdProjectDto bdProjectDto) {
        if (StringUtils.isEmpty(bdProjectDto.getName())) {
            return Result.error("基础数据-项目名称不能为空", bdProjectDto);
        }

        if (bdProjectDto.getYear() == null) {
            return Result.error("基础数据-项目年度不能为空", bdProjectDto);
        }

        BdProject bdProject = bdProjectService
                .lambdaQuery()
                .eq(BdProject::getName, bdProjectDto.getName())
                .eq(BdProject::getYear, bdProjectDto.getYear())
                .one();

        if (bdProject != null) {
            return Result.error("”" + bdProject.getName() + "“与已有项目重复", bdProjectDto);
        }

        try {
            bdProjectService.save(bdProjectDto);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdProjectDto);
        }

        return Result.OK("新增：单个：成功！", bdProjectDto);
    }

    /**
     * 更新：单个
     *
     * @param bdProjectDto
     * @return
     */
    @AutoLog(value = "更新：单个")
    @ApiOperation(value = "更新：单个", notes = "更新：单个")
    @PostMapping(value = "/updateById")
    public Result<BdProjectDto> updateById(@RequestBody BdProjectDto bdProjectDto) {
        if (StringUtils.isEmpty(bdProjectDto.getBdProjectId())) {
            return Result.error("基础数据-项目标识不能为空值", bdProjectDto);
        }

        if (StringUtils.isEmpty(bdProjectDto.getName())) {
            return Result.error("基础数据-项目名称不能为空", bdProjectDto);
        }

        if (bdProjectDto.getYear() == null) {
            return Result.error("基础数据-项目年度不能为空", bdProjectDto);
        }

        BdProject bdProject = bdProjectService
                .lambdaQuery()
                .eq(BdProject::getName, bdProjectDto.getName())
                .eq(BdProject::getYear, bdProjectDto.getYear())
                .one();

        if (bdProject != null) {
            if (!StringUtils.equals(bdProject.getBdProjectId(), bdProjectDto.getBdProjectId())) {
                return Result.error("”" + bdProject.getName() + "“与已有项目重复", bdProjectDto);
            }
        }

        try {
            bdProjectService.updateById(bdProjectDto);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), bdProjectDto);
        }

        return Result.OK("更新：单个：成功!", bdProjectDto);
    }

    /**
     * 删除：单个
     *
     * @param bdProjectId
     * @return
     */
    @AutoLog(value = "删除：单个")
    @ApiOperation(value = "删除：单个", notes = "删除：单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdProjectId", value = "基础数据-项目标识", dataTypeClass = String.class)
    )
    @DeleteMapping(value = "/removeByBdProjectWfInstanceId")
    public Result<?> removeByBdProjectWfInstanceId(@RequestParam(name = "bdProjectId") String bdProjectId) {
        if (StringUtils.isEmpty(bdProjectId)) {
            return Result.error("基础数据-项目标识不能为空值");
        }

        bdProjectService.removeById(bdProjectId);

        return Result.OK("删除成功!");
    }

    /**
     * 删除：批量
     *
     * @param bdProjectIds
     * @return
     */
    @AutoLog(value = "删除：批量")
    @ApiOperation(value = "删除：批量", notes = "删除：批量")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "bdProjectIds", value = "基础数据-项目标识集合，逗号分隔", dataTypeClass = String.class, example = "1,2,3")
    )
    @DeleteMapping(value = "/removeByBdProjectWfInstanceIds")
    public Result<?> removeByBdProjectWfInstanceIds(@RequestParam(name = "bdProjectIds") String bdProjectIds) {
        if (StringUtils.isEmpty(bdProjectIds)) {
            return Result.error("基础数据-项目标识集合不能为空值");
        }

        this.bdProjectService.removeByIds(Arrays.asList(bdProjectIds.split(",")));

        return Result.OK("删除：批量成功!");
    }

    /**
     * 查询：单个
     *
     * @param bdProjectId
     * @return
     */
    @AutoLog(value = "查询：单个")
    @ApiOperation(value = "查询：单个", notes = "查询：单个")
    @GetMapping(value = "/getByBdProjectId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdProjectId", value = "基础数据-项目标识", dataTypeClass = String.class),
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    })
    public Result<BdProjectDto> getByBdProjectId(@RequestParam(name = "bdProjectId") String bdProjectId, @RequestParam(name = "wfInstanceId") String wfInstanceId) {
        BdProjectDto bdProjectDto = new BdProjectDto();

        if (StringUtils.isEmpty(bdProjectId)) {
            return Result.error("基础数据-项目标识不能为空值", bdProjectDto);
        }

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", bdProjectDto);
        }

        BdProject bdProject = bdProjectService.getById(bdProjectId);

        if (bdProject == null) {
            return Result.error("未查询到项目数据", bdProjectDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("流程-流程实例标识查询不到对应的流程-流程实例", bdProjectDto);
        }

        BeanUtils.copyProperties(bdProject, bdProjectDto);

        if (bdProjectDto.getProjectInitiationTime() != null) {
            bdProjectDto.setProjectInitiationTimeString(DateFormatUtils.format(bdProjectDto.getProjectInitiationTime(), "yyyy-MM-dd HH:mm"));
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getSystemGradingCode())) {
            RunnableUtils.execute(() -> {
                String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("project_rank", bdProjectDto.getSystemGradingCode());

                bdProjectDto.setSystemGradingName(systemGradingName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getSystemTypeCode())) {
            RunnableUtils.execute(() -> {
                String systemTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_type", bdProjectDto.getSystemTypeCode());

                bdProjectDto.setSystemTypeName(systemTypeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getExpenditureTypeCode())) {
            RunnableUtils.execute(() -> {
                String expenditureTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_spending_type", bdProjectDto.getExpenditureTypeCode());

                bdProjectDto.setExpenditureTypeName(expenditureTypeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getServiceOpeningScopeCode())) {
            RunnableUtils.execute(() -> {
                String serviceOpeningScopeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("system_service_coverage", bdProjectDto.getServiceOpeningScopeCode());

                bdProjectDto.setServiceOpeningScopeName(serviceOpeningScopeName);
            });
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getProjectLeaderCode())) {
            LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .getUserByName(bdProjectDto.getProjectLeaderCode());

            if (loginUser != null) {
                bdProjectDto.setProjectLeaderName(loginUser.getRealname());
            }
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getOmPrincipalCode())) {
            LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .getUserByName(bdProjectDto.getOmPrincipalCode());

            if (loginUser != null) {
                bdProjectDto.setOmPrincipalName(loginUser.getRealname());
            }
        }

        if (StringUtils.isNotEmpty(bdProjectDto.getOmDepartmentCode())) {
            RunnableUtils.execute(() -> {
                String omDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getDepartNameByOrgCode(bdProjectDto.getOmDepartmentCode());

                bdProjectDto.setOmDepartmentName(omDepartmentName);
            });
        }

        RunnableUtils.execute(() -> {
            Map<String, String> inWfCodeMap = Maps.newHashMap();

            inWfCodeMap.put("project_check", "project_rank");

            List<BdFileDto> bdFileList = bdFileService.listByWfCode(wfInstance.getWfCode(),
                    wfInstance.getWfInstanceId(),
                    "bd_project",
                    bdProjectId,
                    inWfCodeMap.get(wfInstance.getWfCode()));

            bdProjectDto.setBdFileList(bdFileList);
        });

        RunnableUtils.waitExecute();

        return Result.OK(bdProjectDto);
    }

    /**
     * 查询：总数：发起人编号
     *
     * @param initiatorCode
     * @return
     */
    @AutoLog(value = "查询：总数：发起人编号")
    @ApiOperation(value = "查询：总数：发起人编号", notes = "首页-项目数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "initiatorCode", value = "当起人编号", dataTypeClass = String.class)
    })
    @GetMapping(value = "/countByInitiatorCode")
    public Result<Integer> countByInitiatorCode(@RequestParam(name = "initiatorCode", required = false) String initiatorCode) {
        Integer count;

        if (StringUtils.isEmpty(initiatorCode)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

            initiatorCode = sysUser.getUsername();
        }

        count = bdProjectService.countByInitiatorCode(initiatorCode);

        return Result.OK(count);
    }

    /**
     * 查询：分页：流程编号
     *
     * @param bdProjectDto
     * @return
     */
    @AutoLog(value = "查询：分页：流程编号")
    @ApiOperation(value = "查询：分页：流程编号", notes = "查询：分页：流程编号")
    @PostMapping(value = "/pageByWfCode")
    public Result<IPage<BdProjectDto>> pageByWfCode(@RequestBody BdProjectDto bdProjectDto) {
        Map<String, List<String>> inWfCodesMap = Maps.newHashMap();

        inWfCodesMap.put("project_check", Lists.newArrayList("project_rank"));
        inWfCodesMap.put("network_access", Lists.newArrayList("project_check", "ineed_check"));
        inWfCodesMap.put("accept", Lists.newArrayList("network_access"));
        inWfCodesMap.put("alter_report", Lists.newArrayList("network_access"));
        inWfCodesMap.put("operation", Lists.newArrayList("network_access"));
        inWfCodesMap.put("risk_assessment", Lists.newArrayList("network_access"));
        inWfCodesMap.put("disposal", Lists.newArrayList("network_access"));
        inWfCodesMap.put("network_exit", Lists.newArrayList("network_access"));

        bdProjectDto.setInWfCodes(inWfCodesMap.get(bdProjectDto.getWfCode()));

        IPage<BdProjectDto> page = bdProjectService.pageByWfCode(bdProjectDto);

        page.getRecords().forEach(it -> {
            if (it.getProjectInitiationTime() != null) {
                it.setProjectInitiationTimeString(DateFormatUtils.format(it.getProjectInitiationTime(), "yyyy-MM-dd HH:mm"));
            }

            if (StringUtils.isNotEmpty(it.getSystemGradingCode())) {
                RunnableUtils.execute(() -> {
                    String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("project_rank", it.getSystemGradingCode());

                    it.setSystemGradingName(systemGradingName);
                });
            }

            if (StringUtils.isNotEmpty(it.getSystemTypeCode())) {
                RunnableUtils.execute(() -> {
                    String systemTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_type", it.getSystemTypeCode());

                    it.setSystemTypeName(systemTypeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getExpenditureTypeCode())) {
                RunnableUtils.execute(() -> {
                    String expenditureTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_spending_type", it.getExpenditureTypeCode());

                    it.setExpenditureTypeName(expenditureTypeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getServiceOpeningScopeCode())) {
                RunnableUtils.execute(() -> {
                    String serviceOpeningScopeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_service_coverage", it.getServiceOpeningScopeCode());

                    it.setServiceOpeningScopeName(serviceOpeningScopeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getProjectLeaderCode())) {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(it.getProjectLeaderCode());

                if (loginUser != null) {
                    it.setProjectLeaderName(loginUser.getRealname());
                }

                RunnableUtils.execute(() -> {
                    String omDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .getDepartNameByOrgCode(loginUser.getOrgCode());

                    it.setProjectDepartmentName(omDepartmentName);
                });
            }

            if (StringUtils.isNotEmpty(it.getOmPrincipalCode())) {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(it.getOmPrincipalCode());

                if (loginUser != null) {
                    it.setOmPrincipalName(loginUser.getRealname());
                }
            }

            if (StringUtils.isNotEmpty(it.getOmDepartmentCode())) {
                RunnableUtils.execute(() -> {
                    String omDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .getDepartNameByOrgCode(it.getOmDepartmentCode());

                    it.setOmDepartmentName(omDepartmentName);
                });
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(page);
    }

    /**
     * 查询：分页：流程编号
     *
     * @param bdProjectDto
     * @return
     */
    @AutoLog(value = "查询：分页：流程编号及流程节点编号")
    @ApiOperation(value = "查询：分页：流程编号及流程节点编号", notes = "查询：分页：流程编号及流程节点编号")
    @PostMapping(value = "/pageByWfCodeAndWfNodeCode")
    public Result<IPage<BdProjectDto>> pageByWfCodeAndWfNodeCode(@RequestBody BdProjectDto bdProjectDto) {
        if (StringUtils.isEmpty(bdProjectDto.getWfCode())) {
            return Result.error("流程编号不能为空值", null);
        }

        IPage<BdProjectDto> page = bdProjectService.pageByWfCodeAndWfNodeCode(bdProjectDto);

        page.getRecords().forEach(it -> {
            if (it.getProjectInitiationTime() != null) {
                it.setProjectInitiationTimeString(DateFormatUtils.format(it.getProjectInitiationTime(), "yyyy-MM-dd HH:mm"));
            }

            if (StringUtils.isNotEmpty(it.getSystemGradingCode())) {
                RunnableUtils.execute(() -> {
                    String systemGradingName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("project_rank", it.getSystemGradingCode());

                    it.setSystemGradingName(systemGradingName);
                });
            }

            if (StringUtils.isNotEmpty(it.getSystemTypeCode())) {
                RunnableUtils.execute(() -> {
                    String systemTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_type", it.getSystemTypeCode());

                    it.setSystemTypeName(systemTypeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getExpenditureTypeCode())) {
                RunnableUtils.execute(() -> {
                    String expenditureTypeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_spending_type", it.getExpenditureTypeCode());

                    it.setExpenditureTypeName(expenditureTypeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getServiceOpeningScopeCode())) {
                RunnableUtils.execute(() -> {
                    String serviceOpeningScopeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("system_service_coverage", it.getServiceOpeningScopeCode());

                    it.setServiceOpeningScopeName(serviceOpeningScopeName);
                });
            }

            if (StringUtils.isNotEmpty(it.getProjectLeaderCode())) {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(it.getProjectLeaderCode());

                if (loginUser != null) {
                    it.setProjectLeaderName(loginUser.getRealname());
                }
            }

            if (StringUtils.isNotEmpty(it.getOmPrincipalCode())) {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(it.getOmPrincipalCode());

                if (loginUser != null) {
                    it.setOmPrincipalName(loginUser.getRealname());
                }
            }

            if (StringUtils.isNotEmpty(it.getOmDepartmentCode())) {
                RunnableUtils.execute(() -> {
                    String omDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .getDepartNameByOrgCode(it.getOmDepartmentCode());

                    it.setOmDepartmentName(omDepartmentName);
                });
            }

            RunnableUtils.execute(() -> {
                List<BdFileDto> bdFileList = bdFileService.listByWfCode(bdProjectDto.getWfCode(),
                        it.getWfInstanceId(),
                        "bd_project",
                        it.getBdProjectId());

                it.setBdFileList(bdFileList);
            });

            if (StringUtils.isNotEmpty(it.getWfNodeCode())) {
                RunnableUtils.execute(() -> {
                    String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict(bdProjectDto.getWfCode() + "_node", it.getWfNodeCode());

                    it.setWfNodeName(wfNodeName);
                });
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(page);
    }

    /**
     * 查询：分页：流程编号
     *
     * @param bdProjectDto
     * @return
     */
    @AutoLog(value = "查询：分页：流程编号及流程实例状态编号")
    @ApiOperation(value = "查询：分页：流程编号及流程实例状态编号", notes = "查询：分页：流程编号及流程实例状态编号")
    @PostMapping(value = "/pageByWfCodeAndWfInstanceStateCode")
    public Result<IPage<BdProjectDto>> pageByWfCodeAndWfInstanceStateCode(@RequestBody BdProjectDto bdProjectDto) {
        IPage<BdProjectDto> page = bdProjectService.pageByWfCodeAndWfInstanceStateCode(bdProjectDto);

        page.getRecords().forEach(it -> {
            if (it.getProjectInitiationTime() != null) {
                it.setProjectInitiationTimeString(DateFormatUtils.format(it.getProjectInitiationTime(), "yyyy-MM-dd HH:mm"));
            }

            if (StringUtils.isNotEmpty(it.getProjectRankWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getProjectRankWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setProjectRankStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getProjectCheckWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getProjectCheckWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setProjectCheckStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getNetworkAccessWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getNetworkAccessWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setNetworkAccessStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getAcceptWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getAcceptWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setAcceptStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getAlterReportWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getAlterReportWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setAlterReportStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getOperationWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getOperationWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setOperationStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getRiskAssessmentWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getRiskAssessmentWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setRiskAssessmentStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getDisposalWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getDisposalWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setDisposalStateName(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }

            if (StringUtils.isNotEmpty(it.getNetworkExitWfInstanceId())) {
                RunnableUtils.execute(() -> {
                    WfInstance wfInstance = wfInstanceService.getById(it.getNetworkExitWfInstanceId());

                    if (wfInstance == null) {
                        return;
                    }

                    it.setNetworkExitWfInstanceId(SpringContextUtils.getBean(ISysBaseAPI.class)
                            .translateDict("process_status", wfInstance.getStateCode()));
                });
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(page);
    }

    /**
     * 查询：列表：系统定级总数
     *
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "查询：列表：系统定级总数")
    @ApiOperation(value = "查询：列表：系统定级总数", notes = "总览图-系统定级")
    @GetMapping(value = "/listBySystemGradingCode")
    public Result<List<ListBySystemGradingCodeResponse>> listBySystemGradingCode() {
        List<ListBySystemGradingCodeResponse> listBySystemGradingCodeResponseList = bdProjectService.listBySystemGradingCode()
                .stream()
                .map(it -> new ListBySystemGradingCodeResponse()
                        .setText(MapUtils.getString(it, "text"))
                        .setCode(MapUtils.getString(it, "code"))
                        .setCount(MapUtils.getLong(it, "bd_project_count")))
                .collect(Collectors.toList());

        return Result.OK(listBySystemGradingCodeResponseList);
    }
}
