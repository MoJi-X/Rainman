package com.rainman.modules.ists.wf.function.accept;

import com.google.common.collect.Lists;
import com.rainman.modules.ists.bd.service.IBdFileService;
import com.rainman.modules.ists.bd.service.IBdProjectWfInstanceService;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class UserTask10Consumer implements BiConsumer<WfSubmitDto, Map<String, Object>> {
    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    private IBdProjectWfInstanceService bdProjectWfInstanceService;

    @SneakyThrows
    @Override
    public void accept(WfSubmitDto wfSubmitDto, Map<String, Object> variables) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(wfSubmitDto.getWfNextNodeCandidateUsers())) {
            throw new Exception("流程下一个节点处理人编号集合不能为空值");
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(wfSubmitDto.getBdProjectId())) {
            throw new Exception("项目标识不能为空值");
        }

        Task task = (Task) variables.get("task");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        if (processInstance == null) {
            throw new Exception("查询不对对应的流程引擎实例");
        }

        if (StringUtils.isNotBlank(wfSubmitDto.getBdProjectId())) {
            bdProjectWfInstanceService.saveOrUpdate(processInstance.getBusinessKey(), wfSubmitDto.getBdProjectId());
        }

        List<Map<String, Object>> mapList = bdProjectWfInstanceService.listByWfInstanceId(processInstance.getBusinessKey());

        if (mapList.isEmpty()) {
            throw new Exception("没有录入项目");
        }

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "提交审核");

        taskService.setAssignee(task.getId(), loginUser.getUsername());

        variables.clear();

        List<String> wfNextNodeCandidateUsers = Lists.newArrayList(StringUtils.split(wfSubmitDto.getWfNextNodeCandidateUsers(), ","));

        variables.put("last_candidate_users", wfNextNodeCandidateUsers);

        taskService.complete(task.getId(), variables);
    }
}
