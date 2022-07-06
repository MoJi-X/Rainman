package com.rainman.modules.ists.wf.function.ineed_check;

import com.rainman.modules.ists.bd.entity.BdProject;
import com.rainman.modules.ists.bd.entity.BdProjectWfInstance;
import com.rainman.modules.ists.bd.service.IBdProjectService;
import com.rainman.modules.ists.bd.service.IBdProjectWfInstanceService;
import com.rainman.modules.ists.wf.dto.WfSubmitDto;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.function.project_rank.UserTask20Consumer;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ineed_check_user_task_50_consumer")
public class UserTask50Consumer extends UserTask20Consumer {
    @Autowired
    private IBdProjectWfInstanceService bdProjectWfInstanceService;

    @Autowired
    private IBdProjectService bdProjectService;

    @Autowired
    RuntimeService runtimeService;

    @SneakyThrows
    @Override
    public void accept(WfSubmitDto wfSubmitDto, Map<String, Object> variables) {
        if (!"true".equals(StringUtils.defaultString(wfSubmitDto.getApprove(), "true"))) {
            return;
        }

        Task task = (Task) variables.get("task");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        if (processInstance == null) {
            throw new Exception("查询不对对应的流程引擎实例");
        }

        List<String> bdProjectIds = bdProjectWfInstanceService.lambdaQuery()
                .eq(BdProjectWfInstance::getWfInstanceId, processInstance.getBusinessKey())
                .list()
                .stream()
                .map(it -> it.getBdProjectId())
                .collect(Collectors.toList());

        WfInstance wfInstance = SpringContextUtils.getBean(IWfInstanceService.class).getById(processInstance.getBusinessKey());

        bdProjectService.lambdaUpdate()
                .set(BdProject::getDataStateCode, "finished")
                .set(BdProject::getInitiatorCode, wfInstance.getInitiatorCode())
                .set(BdProject::getInitiatorDepartmentCode, wfInstance.getInitiatorDepartmentCode())
                .in(BdProject::getBdProjectId, bdProjectIds)
                .update();

        super.accept(wfSubmitDto, variables);
    }
}
