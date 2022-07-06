package com.rainman.modules.ists.wf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rainman.modules.ists.wf.dto.WfCommentDto;
import com.rainman.modules.ists.wf.dto.WfInstanceDto;
import com.rainman.modules.ists.wf.dto.WfNodeInfoDto;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.response.wf.CountByBdProjectResponse;
import com.rainman.modules.ists.wf.response.wf.ListCountByOrgForWfResponse;
import com.rainman.modules.ists.wf.response.wf.ListCountByWfThisYearRangeMonthResponse;
import com.rainman.modules.ists.wf.response.wf.PageByDoneRecordResponse;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import com.rainman.util.RunnableUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 流程
 * @Author: yao-hai-tao
 * @Date: 2021-11-25
 * @Version: V1.0
 */
@Api(tags = "流程")
@RestController
@RequestMapping("/ists.wf")
@Slf4j
public class WfController extends JeecgController<WfInstance, IWfInstanceService> {
    @Autowired
    private IWfInstanceService wfInstanceService;

    /**
     * 删除：单个：流程实例
     *
     * @param wfInstanceId
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "删除：单个：流程实例")
    @ApiOperation(value = "删除：单个：流程实例", notes = "删除：单个：流程实例")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @DeleteMapping(value = "/removeByWfInstanceId")
    public Result<?> removeByWfInstanceId(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值");
        }

        try {
            wfInstanceService.removeByWfInstanceId(wfInstanceId);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));
        }

        return Result.OK();
    }

    /**
     * 暂存：单个：流程实例
     *
     * @param wfInstanceId
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "暂存：单个：流程实例")
    @ApiOperation(value = "暂存：单个：流程实例", notes = "暂存：单个：流程实例")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @PutMapping(value = "/temporaryStorageByWfInstanceId")
    public Result<?> temporaryStorageByWfInstanceId(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值");
        }

        try {
            wfInstanceService.temporaryStorageByWfInstanceId(wfInstanceId);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()));
        }

        return Result.OK();
    }

    /**
     * 发起
     *
     * @param wfCode 流程编号
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "发起")
    @ApiOperation(value = "发起", notes = "发起")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfCode", value = "系统定级:project_rank；立项审批:project_check；特需审批:ineed_check；建设入网:network_access；安全验收:accept；变更报备:alter_report；安全运维:operation；风险评估:risk_assessment；处置备查:disposal；安全退网:network_exit", example = "project_rank", dataTypeClass = String.class)
    )
    @PostMapping(value = "/launch")
    public Result<WfInstance> launch(@RequestParam(name = "wfCode") String wfCode) {
        WfInstance wfInstance = new WfInstance();

        if (StringUtils.isEmpty(wfCode)) {
            return Result.error("流程编号未传值", wfInstance);
        }

        String wfName = SpringContextUtils.getBean(ISysBaseAPI.class).translateDict("process_number", wfCode);

        if (StringUtils.isEmpty(wfName)) {
            return Result.error("不支持的流程编号", wfInstance);
        }

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String wfInstanceName = new StringBuffer("【")
                .append(wfName)
                .append("流程】(")
                .append(loginUser.getRealname())
                .append(")")
                .append(DateFormatUtils.format(new Date(), "yyyy年MM月dd日 HH时mm分"))
                .toString();

        wfInstance.setWfCode(wfCode);
        wfInstance.setName(wfInstanceName);
        wfInstance.setInitiatorCode(loginUser.getUsername());
        wfInstance.setInitiatorDepartmentCode(loginUser.getOrgCode());
        wfInstance.setInitiationDate(new Date());

        try {
            wfInstance = wfInstanceService.launch(wfInstance);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), wfInstance);
        }

        return Result.OK(wfInstance);
    }

    /**
     * 提交
     *
     * @param wfSubmitDto 提交
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "提交")
    @ApiOperation(value = "提交", notes = "提交")
    @PostMapping(value = "/submit")
    public Result<WfSubmitDto> submit(@RequestBody WfSubmitDto wfSubmitDto) {
        if (StringUtils.isEmpty(wfSubmitDto.getTaskId())) {
            return Result.error("流程任务标识不能为空值", wfSubmitDto);
        }

        Task task = SpringContextUtils.getBean(TaskService.class).createTaskQuery()
                .taskId(wfSubmitDto.getTaskId())
                .singleResult();

        if (task == null) {
            return Result.error("未查询到流程任务数据", wfSubmitDto);
        }

        Map<String, Object> variables = Maps.newHashMap();

        variables.put("task", task);

        try {
            wfInstanceService.submit(wfSubmitDto, variables);
        } catch (Exception ex) {
            return Result.error(StringUtils.defaultString(ex.getMessage(), ex.getCause().getMessage()), wfSubmitDto);
        }

        return Result.OK(wfSubmitDto);
    }

    /**
     * 查询：单个
     *
     * @param wfInstanceId
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "查询：单个")
    @ApiOperation(value = "查询：单个", notes = "查询：单个")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @GetMapping(value = "/getByWfInstanceId")
    public Result<WfInstanceDto> getByWfInstanceId(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        WfInstanceDto wfInstanceDto = new WfInstanceDto();

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", wfInstanceDto);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("未查询到流程实例数据", wfInstanceDto);
        }

        BeanUtils.copyProperties(wfInstance, wfInstanceDto);

        RunnableUtils.execute(() -> {
            wfInstanceDto.setWfName(SpringContextUtils.getBean(ISysBaseAPI.class).translateDict("process_number", wfInstanceDto.getWfCode()));
        });

        if (StringUtils.isNotEmpty(wfInstanceDto.getInitiatorCode())) {
            LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                    .getUserByName(wfInstanceDto.getInitiatorCode());

            if (loginUser != null) {
                wfInstanceDto.setInitiatorName(loginUser.getRealname());
            }
        }

        if (StringUtils.isNotEmpty(wfInstanceDto.getInitiatorDepartmentCode())) {
            RunnableUtils.execute(() -> {
                String initiatorDepartmentName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getDepartNameByOrgCode(wfInstanceDto.getInitiatorDepartmentCode());

                wfInstanceDto.setInitiatorDepartmentName(initiatorDepartmentName);
            });
        }

        if (StringUtils.isNotEmpty(wfInstanceDto.getStateCode())) {
            RunnableUtils.execute(() -> {
                String stateName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("process_status", wfInstanceDto.getStateCode());

                wfInstanceDto.setStateName(stateName);
            });
        }

        if (StringUtils.isNotEmpty(wfInstanceDto.getWfNodeCode())) {
            RunnableUtils.execute(() -> {
                String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict(wfInstanceDto.getWfCode() + "_node", wfInstanceDto.getWfNodeCode());

                wfInstanceDto.setWfNodeName(wfNodeName);
            });
        }

        if (wfInstanceDto.getInitiationDate() != null) {
            wfInstanceDto.setInitiationDateString(DateFormatUtils.format(wfInstanceDto.getInitiationDate(), "yyyy年MM月dd日 HH时mm分"));
        }

        RunnableUtils.execute(() -> {
            List<Task> taskList = SpringContextUtils.getBean(TaskService.class).createTaskQuery()
                    .processInstanceId(wfInstanceDto.getProcessId())
                    .list();

            if (!taskList.isEmpty()) {
                wfInstanceDto.setTaskId(taskList.get(0).getId());
            }
        });

        RunnableUtils.waitExecute();

        return Result.OK(wfInstanceDto);
    }

    /**
     * 查询：列表：节点信息
     *
     * @param wfInstanceId
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "查询：列表：流程进度")
    @ApiOperation(value = "查询：列表：流程进度", notes = "查询：列表：流程进度")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @GetMapping(value = "/listNodeInfo")
    public Result<List<WfNodeInfoDto>> listNodeInfo(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        List<WfNodeInfoDto> wfNodeInfoDtoList = Lists.newArrayList();

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", wfNodeInfoDtoList);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("未查询到流程实例数据", wfNodeInfoDtoList);
        }

        List<DictModel> dictModelList = SpringContextUtils.getBean(ISysBaseAPI.class).queryDictItemsByCode(wfInstance.getWfCode() + "_node");

        List<HistoricTaskInstance> historicTaskInstanceList = SpringContextUtils.getBean(HistoryService.class).createHistoricTaskInstanceQuery()
                .processInstanceId(wfInstance.getProcessId())
                .orderByTaskCreateTime()
                .asc()
                .list();

        List<Task> taskList = SpringContextUtils.getBean(TaskService.class).createTaskQuery()
                .processInstanceId(wfInstance.getProcessId())
                .list();

        for (DictModel dictModel : dictModelList) {
            WfNodeInfoDto wfNodeInfoDto = new WfNodeInfoDto();

            wfNodeInfoDtoList.add(wfNodeInfoDto);

            wfNodeInfoDto.setName(dictModel.getText());
            wfNodeInfoDto.setCode(dictModel.getValue());
            wfNodeInfoDto.setDescription(dictModel.getDescription());

            String taskDefinitionKey = "usertask_" + dictModel.getValue();

            for (Task task : taskList) {
                wfNodeInfoDto.setActivation(taskDefinitionKey.equals(task.getTaskDefinitionKey()));
            }

            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
                if (!taskDefinitionKey.equals(historicTaskInstance.getTaskDefinitionKey())) {
                    continue;
                }

                wfNodeInfoDto.setLastAssigneeCode(historicTaskInstance.getAssignee());

                wfNodeInfoDto.setLastHandleTimeString(DateFormatUtils.format(historicTaskInstance.getStartTime(), "yyyy年MM月dd日 HH时mm分"));
            }

            if (StringUtils.isNotEmpty(wfNodeInfoDto.getLastAssigneeCode())) {
                RunnableUtils.execute(() -> {
                    LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .getUserByName(wfNodeInfoDto.getLastAssigneeCode());

                    if (loginUser != null) {
                        wfNodeInfoDto.setLastAssigneeName(loginUser.getRealname());
                    }
                });
            }
        }

        RunnableUtils.waitExecute();

        return Result.OK(wfNodeInfoDtoList);
    }

    /**
     * 查询：列表：意见
     *
     * @param wfInstanceId
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "查询：列表：意见")
    @ApiOperation(value = "查询：列表：意见", notes = "查询：列表：意见")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "wfInstanceId", value = "流程-流程实例标识", dataTypeClass = String.class)
    )
    @GetMapping(value = "/listComment")
    public Result<List<WfCommentDto>> listComment(@RequestParam(name = "wfInstanceId") String wfInstanceId) {
        List<WfCommentDto> wfCommentDtoList = Lists.newArrayList();

        if (StringUtils.isEmpty(wfInstanceId)) {
            return Result.error("流程-流程实例标识不能为空值", wfCommentDtoList);
        }

        WfInstance wfInstance = wfInstanceService.getById(wfInstanceId);

        if (wfInstance == null) {
            return Result.error("未查询流程实例数据", wfCommentDtoList);
        }

        List<Comment> commentList = SpringContextUtils.getBean(TaskService.class).getProcessInstanceComments(wfInstance.getProcessId());

        if (commentList.isEmpty()) {
            return Result.OK(wfCommentDtoList);
        }

        List<DictModel> dictModelList = SpringContextUtils.getBean(ISysBaseAPI.class).queryDictItemsByCode(wfInstance.getWfCode() + "_node");

        List<HistoricTaskInstance> historicTaskInstanceList = SpringContextUtils.getBean(HistoryService.class).createHistoricTaskInstanceQuery()
                .processInstanceId(wfInstance.getProcessId())
                .orderByTaskCreateTime()
                .asc()
                .list();

        for (Comment comment : commentList) {
            WfCommentDto wfCommentDto = new WfCommentDto();

            wfCommentDtoList.add(wfCommentDto);

            wfCommentDto.setMessage(comment.getFullMessage());
            wfCommentDto.setLastAssigneeCode(comment.getUserId());
            wfCommentDto.setLastHandleTimeString(DateFormatUtils.format(comment.getTime(), "yyyy年MM月dd日 HH时mm分"));

            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
                if (!historicTaskInstance.getId().equals(comment.getTaskId())) {
                    continue;
                }

                String wfNodeCode = org.apache.commons.lang.StringUtils.substring(historicTaskInstance.getTaskDefinitionKey(), "usertask_".length());

                wfCommentDto.setCode(wfNodeCode);

                wfCommentDto.setLastAssigneeCode(historicTaskInstance.getAssignee());
            }

            for (DictModel dictModel : dictModelList) {
                if (!StringUtils.equals(dictModel.getValue(), wfCommentDto.getCode())) {
                    continue;
                }

                wfCommentDto.setName(dictModel.getText());
                wfCommentDto.setDescription(dictModel.getDescription());
            }

            if (StringUtils.isNotEmpty(wfCommentDto.getLastAssigneeCode())) {
                RunnableUtils.execute(() -> {
                    LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                            .getUserByName(wfCommentDto.getLastAssigneeCode());

                    if (loginUser != null) {
                        wfCommentDto.setLastAssigneeName(loginUser.getRealname());
                    }
                });
            }
        }

        RunnableUtils.waitExecute();

        Collections.reverse(wfCommentDtoList);

        return Result.OK(wfCommentDtoList);
    }

    /**
     * 查询：分页：待办
     *
     * @return
     */
    @AutoLog(value = "查询：分页：待办")
    @ApiOperation(value = "查询：分页：待办", notes = "首页-待办事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页号", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "pageSize", value = "页大小", example = "10", dataTypeClass = Long.class)
    })
    @PostMapping(value = "/pageByToDo")
    public Result<IPage<PageByDoneRecordResponse>> pageByToDo(@RequestParam(name = "pageNo", defaultValue = "1") Long pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return this.pageBy(pageNo, pageSize, "false");
    }

    /**
     * 查询：分页：已办
     *
     * @return
     */
    @AutoLog(value = "查询：分页：已办")
    @ApiOperation(value = "查询：分页：已办", notes = "首页-已办事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页号", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "pageSize", value = "页大小", example = "10", dataTypeClass = Long.class)
    })
    @PostMapping(value = "/pageByDone")
    public Result<IPage<PageByDoneRecordResponse>> pageByDone(@RequestParam(name = "pageNo", defaultValue = "1") Long pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return this.pageBy(pageNo, pageSize, "true");
    }

    private Result<IPage<PageByDoneRecordResponse>> pageBy(Long pageNo, Long pageSize, String isHistory) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        IPage<WfInstanceDto> page = wfInstanceService.pageByUsername(pageNo, pageSize, sysUser.getUsername(), isHistory);

        IPage<PageByDoneRecordResponse> newPage = page.convert(it -> {
            PageByDoneRecordResponse pageByDoneRecordResponse = new PageByDoneRecordResponse();

            BeanUtils.copyProperties(it, pageByDoneRecordResponse);

            RunnableUtils.execute(() -> {
                String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict(pageByDoneRecordResponse.getWfCode() + "_node", pageByDoneRecordResponse.getWfNodeCode());

                pageByDoneRecordResponse.setWfNodeName(wfNodeName);
            });

            RunnableUtils.execute(() -> {
                String wfName = SpringContextUtils.getBean(ISysBaseAPI.class).translateDict("process_number", pageByDoneRecordResponse.getWfCode());

                pageByDoneRecordResponse.setWfName(wfName);
            });

            RunnableUtils.execute(() -> {
                String stateName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict("process_status", pageByDoneRecordResponse.getStateCode());

                pageByDoneRecordResponse.setStateName(stateName);
            });

            List<HistoricTaskInstance> historicTaskInstanceList = SpringContextUtils.getBean(HistoryService.class).createHistoricTaskInstanceQuery()
                    .processInstanceBusinessKey(it.getWfInstanceId())
                    .orderByTaskCreateTime()
                    .desc()
                    .list();

            if (historicTaskInstanceList.isEmpty()) {
                return pageByDoneRecordResponse;
            }

            HistoricTaskInstance historicTaskInstance = null;

            for (HistoricTaskInstance item : historicTaskInstanceList) {
                if (item.getEndTime() != null) {
                    historicTaskInstance = item;

                    break;
                }
            }

            if (historicTaskInstance == null) {
                return pageByDoneRecordResponse;
            }

            pageByDoneRecordResponse.setPreviousStepEndTimeString(DateFormatUtils.format(historicTaskInstance.getEndTime(), "yyyy年MM月dd日 HH时mm分"));

            String wfNodeCode = org.apache.commons.lang.StringUtils.substring(historicTaskInstance.getTaskDefinitionKey(), "usertask_".length());

            RunnableUtils.execute(() -> {
                String wfNodeName = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .translateDict(it.getWfCode() + "_node", wfNodeCode);

                pageByDoneRecordResponse.setPreviousStepWfNodeName(wfNodeName);
            });

            HistoricTaskInstance hti = historicTaskInstance;

            RunnableUtils.execute(() -> {
                LoginUser loginUser = SpringContextUtils.getBean(ISysBaseAPI.class)
                        .getUserByName(hti.getAssignee());

                if (loginUser != null) {
                    pageByDoneRecordResponse.setPreviousStepAssigneeName(loginUser.getRealname());
                }
            });

            RunnableUtils.execute(() -> {
                List<Comment> commentList = SpringContextUtils.getBean(TaskService.class).getTaskComments(hti.getId());

                if (!commentList.isEmpty()) {
                    pageByDoneRecordResponse.setPreviousStepMessage(commentList.get(0).getFullMessage());
                }
            });

            return pageByDoneRecordResponse;
        });

        RunnableUtils.waitExecute();

        return Result.OK(newPage);
    }

    /**
     * 查询：总数：流程编号
     *
     * @param wfCode         流程编号
     * @param bdFileFlagCode 流程编号
     * @return
     */
    @AutoLog(value = "查询：总数：流程编号")
    @ApiOperation(value = "查询：总数：流程编号", notes = "定级备案总数：数量 -> ('project_rank', '')<br/>" +
            "规划设计总数：数量 -> ('project_check', '')<br/>" +
            "安全入网总数：数量 -> ('network_access', '')<br/>" +
            "4A管控总数：数量 -> ('network_access', '4A_template')<br/>" +
            "安全验收总数：数量 -> ('accept', '')<br/>" +
            "安全运维总数：数量 -> ('operation', '')<br/>" +
            "风险评估总数：数量 -> ('risk_assessment', '')<br/>" +
            "变更报备总数：数量 -> ('alter_report', '')<br/>" +
            "应急处置总数：数量 -> ('disposal', '')<br/>" +
            "安全退网总数：数量 -> ('network_exit', '')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wfCode", value = "流程编号", dataTypeClass = String.class),
            @ApiImplicitParam(name = "bdFileFlagCode", value = "基础数据文件标志编号", dataTypeClass = String.class)
    })
    @GetMapping(value = "/countByWfCodeAndBdFileFlagCode")
    public Result<Integer> countByWfCodeAndBdFileFlagCode(@RequestParam(name = "wfCode") String wfCode,
                                                          @RequestParam(name = "bdFileFlagCode", required = false) String bdFileFlagCode) {
        Integer count = wfInstanceService.countByWfCodeAndBdFileFlagCode(wfCode, bdFileFlagCode);

        return Result.OK(count);
    }

    /**
     * 查询：总数：纳入三同步安全管理总数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：纳入三同步安全管理总数")
    @ApiOperation(value = "查询：总数：纳入三同步安全管理总数", notes = "三同步纳入情况-纳入三同步安全管理总数")
    @GetMapping(value = "/countByWf")
    public Result<Integer> countByWf() {
        Integer count = wfInstanceService.countByInitiationDate(null, null);

        return Result.OK(count);
    }

    /**
     * 查询：总数：今年纳入总数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：今年纳入总数")
    @ApiOperation(value = "查询：总数：今年纳入总数", notes = "三同步纳入情况-今年纳入总数")
    @GetMapping(value = "/countByWfThisYear")
    public Result<Integer> countByWfThisYear() {
        LocalDateTime beginInitiationDate = LocalDateTime
                .now()
                .with(TemporalAdjusters.firstDayOfYear())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        Integer count = wfInstanceService.countByInitiationDate(
                Timestamp.valueOf(beginInitiationDate),
                Timestamp.valueOf(beginInitiationDate.plusYears(1))
        );

        return Result.OK(count);
    }

    /**
     * 查询：总数：当月纳入总数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：当月纳入总数")
    @ApiOperation(value = "查询：总数：当月纳入总数", notes = "三同步纳入情况-当月纳入总数")
    @GetMapping(value = "/countByWfThisMonth")
    public Result<Integer> countByWfThisMonth() {
        LocalDateTime beginInitiationDate = LocalDateTime
                .now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        Integer count = wfInstanceService.countByInitiationDate(
                Timestamp.valueOf(beginInitiationDate),
                Timestamp.valueOf(beginInitiationDate.plusMonths(1))
        );

        return Result.OK(count);
    }

    /**
     * 查询：总数：今年范围各月总数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：今年范围各月总数")
    @ApiOperation(value = "查询：总数：今年范围各月总数", notes = "三同步纳入情况-今年范围各月总数")
    @GetMapping(value = "/listCountByWfThisYearRangeMonth")
    public Result<List<ListCountByWfThisYearRangeMonthResponse>> listCountByWfThisYearRangeMonth() {
        Map<Integer, String> monthMap = Maps.newHashMap();

        monthMap.put(Month.JANUARY.getValue(), "一月");
        monthMap.put(Month.FEBRUARY.getValue(), "二月");
        monthMap.put(Month.MARCH.getValue(), "三月");
        monthMap.put(Month.APRIL.getValue(), "四月");
        monthMap.put(Month.MAY.getValue(), "五月");
        monthMap.put(Month.JUNE.getValue(), "六月");
        monthMap.put(Month.JULY.getValue(), "七月");
        monthMap.put(Month.AUGUST.getValue(), "八月");
        monthMap.put(Month.SEPTEMBER.getValue(), "九月");
        monthMap.put(Month.OCTOBER.getValue(), "十月");
        monthMap.put(Month.NOVEMBER.getValue(), "十一月");
        monthMap.put(Month.DECEMBER.getValue(), "十二月");

        LocalDateTime initiationDate = LocalDateTime
                .now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        if (initiationDate.getMonthValue() == 1) {
            initiationDate = initiationDate.minusYears(1);
        }

        List<ListCountByWfThisYearRangeMonthResponse> list = Lists.newArrayList();

        for (int i = 1; i <= Month.DECEMBER.getValue(); i++) {
            LocalDateTime beginInitiationDate = initiationDate.withMonth(i);

            if (Timestamp.valueOf(beginInitiationDate).getTime() > System.currentTimeMillis()) {
                break;
            }

            ListCountByWfThisYearRangeMonthResponse listCountByWfThisYearRangeMonthResponse = new ListCountByWfThisYearRangeMonthResponse();

            listCountByWfThisYearRangeMonthResponse.setName(monthMap.get(beginInitiationDate.getMonthValue()));

            list.add(listCountByWfThisYearRangeMonthResponse);

            RunnableUtils.execute(() -> {
                Integer count = wfInstanceService.countByInitiationDate(
                        Timestamp.valueOf(beginInitiationDate),
                        Timestamp.valueOf(beginInitiationDate.plusMonths(1))
                );

                listCountByWfThisYearRangeMonthResponse.setValue(count);
            });
        }

        RunnableUtils.waitExecute();

        return Result.OK(list);
    }

    /**
     * 查询：总数：三同步整体概况(同步规划、同步建设、同步运行)
     *
     * @return
     */
    @AutoLog(value = "查询：总数：三同步整体概况(同步规划、同步建设、同步运行)")
    @ApiOperation(value = "查询：总数：三同步整体概况(同步规划、同步建设、同步运行)", notes = "三同步整体概况-同步规划<br/>" +
            "三同步整体概况-同步建设<br/>" +
            "三同步整体概况-同步运行")
    @GetMapping(value = "/countByBdProject")
    public Result<CountByBdProjectResponse> countByBdProject() {
        Map<String, Object> countMap = wfInstanceService.countForBdProject();

        return Result.OK(new CountByBdProjectResponse()
                .setAllCount(MapUtils.getLong(countMap, "all_count"))
                .setPlanCount(MapUtils.getLong(countMap, "plan_count"))
                .setBuildCount(MapUtils.getLong(countMap, "build_count"))
                .setRuntimeCount(MapUtils.getLong(countMap, "runtime_count"))
        );
    }

    /**
     * 查询：总数：全面纳入三同步，投入运行系统数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：全面纳入三同步，投入运行系统数")
    @ApiOperation(value = "查询：总数：全面纳入三同步，投入运行系统数", notes = "三同步整体概况")
    @GetMapping(value = "/countByWfCodeInAllForBdProject")
    public Result<Integer> countByWfCodeInAllForBdProject() {
        Integer count = wfInstanceService.countByWfCodeInAllForBdProject();

        return Result.OK(count);
    }

    /**
     * 查询：总数：未做安全规划、投入建设系统数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：未做安全规划、投入建设系统数")
    @ApiOperation(value = "查询：总数：未做安全规划、投入建设系统数", notes = "三同步整体概况")
    @GetMapping(value = "/countByWfCodeEqIneedCheckForBdProject")
    public Result<Integer> countByWfCodeEqIneedCheckForBdProject() {
        Integer count = wfInstanceService.countByWfCodeEqIneedCheckForBdProject();

        return Result.OK(count);
    }

    /**
     * 查询：总数：做了安全规划，未验收系统数
     *
     * @return
     */
    @AutoLog(value = "查询：总数：做了安全规划，未验收系统数")
    @ApiOperation(value = "查询：总数：做了安全规划，未验收系统数", notes = "三同步整体概况")
    @GetMapping(value = "/countByWfCodeNqAcceptForBdProject")
    public Result<Integer> countByWfCodeNqAcceptForBdProject() {
        Integer count = wfInstanceService.countByWfCodeNqAcceptForBdProject();

        return Result.OK(count);
    }

    /**
     * 查询：总数：未纳入三同步安全管理
     *
     * @return
     */
    @AutoLog(value = "查询：总数：未纳入三同步安全管理")
    @ApiOperation(value = "查询：总数：未纳入三同步安全管理", notes = "三同步整体概况")
    @GetMapping(value = "/countByWfCodeNqIneedCheckForBdProject")
    public Result<Integer> countByWfCodeNqIneedCheckForBdProject() {
        Integer count = wfInstanceService.countByWfCodeNqIneedCheckForBdProject();

        return Result.OK(count);
    }

    /**
     * 查询：列表：三同步部门开展情况
     *
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "查询：列表：三同步部门开展情况")
    @ApiOperation(value = "查询：列表：三同步部门开展情况", notes = "总览图-三同步部门开展情况")
    @GetMapping(value = "/listCountByOrgForWf")
    public Result<List<ListCountByOrgForWfResponse>> listCountByOrgForWf() {
        List<ListCountByOrgForWfResponse> listCountByOrgForWfResponseList = wfInstanceService.listCountByOrgForWf()
                .stream()
                .map(it -> new ListCountByOrgForWfResponse()
                        .setOrgCode(MapUtils.getString(it, "org_code"))
                        .setOrgName(MapUtils.getString(it, "org_name"))
                        .setPlanCount(MapUtils.getLong(it, "plan_count"))
                        .setBuildCount(MapUtils.getLong(it, "build_count"))
                        .setRuntimeCount(MapUtils.getLong(it, "runtime_count")))
                .collect(Collectors.toList());

        return Result.OK(listCountByOrgForWfResponseList);
    }
}