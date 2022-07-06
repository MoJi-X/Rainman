package com.rainman.modules.ists.wf.function.risk_assessment;

import com.google.common.collect.Lists;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import lombok.SneakyThrows;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class UserTask20Consumer implements BiConsumer<WfSubmitDto, Map<String, Object>>, BeanPostProcessor {
    @Autowired
    TaskService taskService;

    @SneakyThrows
    @Override
    public void accept(WfSubmitDto wfSubmitDto, Map<String, Object> variables) {

        Task task = (Task) variables.get("task");

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        taskService.setAssignee(task.getId(), loginUser.getUsername());

        if (StringUtils.isEmpty(wfSubmitDto.getApprove())) {
            wfSubmitDto.setApprove("true");
        } else if (!"true".equals(wfSubmitDto.getApprove())) {
            wfSubmitDto.setApprove("false");
        }

        String message = wfSubmitDto.getMessage();

        if (StringUtils.isEmpty(message)) {
            message = "true".equals(wfSubmitDto.getApprove()) ? "同意" : "不同意";
        }

        taskService.addComment(task.getId(), task.getProcessInstanceId(), message);

        variables.clear();

        variables.put("approve", wfSubmitDto.getApprove());

        List<String> wfNextNodeCandidateUsers = Lists.newArrayList(StringUtils.split(wfSubmitDto.getWfNextNodeCandidateUsers(), ","));

        variables.put("last_candidate_users", wfNextNodeCandidateUsers);

        taskService.complete(task.getId(), variables);
    }
}
