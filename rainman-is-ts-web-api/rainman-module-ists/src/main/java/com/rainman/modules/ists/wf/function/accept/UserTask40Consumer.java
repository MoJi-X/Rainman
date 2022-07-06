package com.rainman.modules.ists.wf.function.accept;

import com.google.common.collect.Lists;
import com.rainman.modules.ists.bd.dto.BdFileDto;
import com.rainman.modules.ists.bd.service.IBdFileService;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.bd.service.IBdProjectWfInstanceService;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class UserTask40Consumer implements BiConsumer<WfSubmitDto, Map<String, Object>> {
    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    private IBdProjectWfInstanceService bdProjectWfInstanceService;

    @Autowired
    private IBdFileService bdFileService;

    protected List<String> flagCodeList = Lists.newArrayList();

    public UserTask40Consumer(List<String> stringList){
        this.flagCodeList = stringList;
    }
    @SneakyThrows
    @Override
    public void accept(WfSubmitDto wfSubmitDto, Map<String, Object> variables) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(wfSubmitDto.getWfNextNodeCandidateUsers())) {
            throw new Exception("流程下一个节点处理人编号集合不能为空值");
        }

        Task task = (Task) variables.get("task");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        if (processInstance == null) {
            throw new Exception("查询不对对应的流程引擎实例");
        }

        List<Map<String, Object>> mapList = bdProjectWfInstanceService.listByWfInstanceId(processInstance.getBusinessKey());

        if (mapList.isEmpty()) {
            throw new Exception("没有录入项目");
        }

        List<String> bdProjectIds = Lists.newArrayList();

        for (Map<String, Object> map : mapList) {
            String bdProjectId = MapUtils.getString(map, "bd_project_id");

            bdProjectIds.add(bdProjectId);

            List<BdFileDto> bdFileList = bdFileService.listByWfCode(processInstance.getProcessDefinitionKey(),
                    processInstance.getBusinessKey(),
                    "bd_project",
                    bdProjectId);

            List<String> notUploadFileFlagNames = bdFileList.stream()
                    .filter(it -> flagCodeList.contains(it.getFlagCode()) && StringUtils.isBlank(it.getBdFileId()))
                    .map(it -> it.getFlagName())
                    .collect(Collectors.toList());

            if (notUploadFileFlagNames.size() > 0) {
                String name = MapUtils.getString(map, "name");

                throw new Exception("”" + name + "“有文件未上传齐全:" + org.apache.commons.lang3.StringUtils.join(notUploadFileFlagNames, ","));
            }
        }

        String message = wfSubmitDto.getMessage();

        if (StringUtils.isEmpty(message)) {
            message = "true".equals(wfSubmitDto.getApprove()) ? "通过" : "不通过";
        }

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        taskService.addComment(task.getId(), task.getProcessInstanceId(), message);

        taskService.setAssignee(task.getId(), loginUser.getUsername());

        variables.clear();

        List<String> wfNextNodeCandidateUsers = Lists.newArrayList(StringUtils.split(wfSubmitDto.getWfNextNodeCandidateUsers(), ","));

        variables.put("last_candidate_users", wfNextNodeCandidateUsers);

        taskService.complete(task.getId(), variables);
    }
}
