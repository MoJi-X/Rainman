package com.rainman.modules.ists.wf.listener;

import com.google.common.collect.Lists;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.SpringContextUtils;

import java.util.List;
import java.util.function.Consumer;

public class DefaultExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String executionBeans = SpringContextUtils.getApplicationContext().getEnvironment()
                .getProperty("rainman.wf.all.execution-beans",
                        String.class,
                        "defaultExecutionConsumer,messageExecutionConsumer,cmccoaExecutionConsumer");

        List<String> executionBeanList = Lists.newArrayList(StringUtils.split(executionBeans, ","));

        for (String executionBean : executionBeanList) {
            if (!SpringContextUtils.getApplicationContext().containsBean(executionBean)) {
                continue;
            }

            Consumer consumer = SpringContextUtils.getBean(executionBean, Consumer.class);

            if (consumer == null) {
                continue;
            }

            consumer.accept(delegateExecution);
        }
    }
}
