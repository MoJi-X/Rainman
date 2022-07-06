package com.rainman.modules.ists.wf.function.all;

import com.google.common.collect.Sets;
import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

@Component("defaultTaskConsumer")
public class DefaultTaskConsumer implements Consumer<DelegateTask> {
    @Override
    public void accept(DelegateTask delegateTask) {
        WfInstance wfInstance = SpringContextUtils.getBean(IWfInstanceService.class).getById(delegateTask.getExecution().getProcessInstanceBusinessKey());

        if (wfInstance == null) {
            return;
        }

        String wfNodeCode = StringUtils.substring(delegateTask.getTaskDefinitionKey(), "usertask_".length());

        if ("create".equals(delegateTask.getEventName())) {
            if (StringUtils.isEmpty(wfInstance.getWfNodeCode())) {
                wfInstance.setStateCode("not_started");
            } else if (!"10".equals(wfInstance.getWfNodeCode())
                    && "10".equals(wfNodeCode)) {
                wfInstance.setStateCode("returned");
            } else if (!"10".equals(wfNodeCode)) {
                wfInstance.setStateCode("in_progress");
                wfInstance.setDataStateCode("use");
            }

            if (wfInstance.getApplyTime() == null) {
                wfInstance.setApplyTime(new Date());
            }

            wfInstance.setWfNodeCode(wfNodeCode);

            SpringContextUtils.getBean(IWfInstanceService.class).updateById(wfInstance);
        } else if ("assignment".equals(delegateTask.getEventName())) {
            if (StringUtils.isBlank(wfInstance.getWfNodeAssigneeCodes())) {
                wfInstance.setWfNodeAssigneeCodes(delegateTask.getAssignee());
            }

            Set<String> wfNodeAssigneeCodes = Sets.newHashSet(StringUtils.split(wfInstance.getWfNodeAssigneeCodes(), ","));

            wfNodeAssigneeCodes.add(delegateTask.getAssignee());

            wfInstance.setWfNodeAssigneeCodes(StringUtils.join(wfNodeAssigneeCodes, ","));

            SpringContextUtils.getBean(IWfInstanceService.class).updateById(wfInstance);
        }
    }
}
