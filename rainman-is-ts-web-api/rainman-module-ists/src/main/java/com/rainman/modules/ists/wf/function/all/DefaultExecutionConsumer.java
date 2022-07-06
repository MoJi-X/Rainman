package com.rainman.modules.ists.wf.function.all;

import com.rainman.modules.ists.wf.entity.WfInstance;
import com.rainman.modules.ists.wf.service.IWfInstanceService;
import org.activiti.engine.delegate.DelegateExecution;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component("defaultExecutionConsumer")
public class DefaultExecutionConsumer implements Consumer<DelegateExecution> {
    @Override
    public void accept(DelegateExecution delegateExecution) {
        WfInstance wfInstance = SpringContextUtils.getBean(IWfInstanceService.class).getById(delegateExecution.getProcessInstanceBusinessKey());

        if (wfInstance == null) {
            return;
        }

        if ("start".equals(delegateExecution.getEventName())) {
            wfInstance.setProcessId(delegateExecution.getProcessInstanceId());

            SpringContextUtils.getBean(IWfInstanceService.class).updateById(wfInstance);
        } else if ("end".equals(delegateExecution.getEventName())) {
            wfInstance.setStateCode("finished");

            SpringContextUtils.getBean(IWfInstanceService.class).updateById(wfInstance);
        }
    }
}
