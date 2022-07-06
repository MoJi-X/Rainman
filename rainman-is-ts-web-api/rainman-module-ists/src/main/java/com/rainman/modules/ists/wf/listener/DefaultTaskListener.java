package com.rainman.modules.ists.wf.listener;

import com.google.common.collect.Lists;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.SpringContextUtils;

import java.util.List;
import java.util.function.Consumer;

public class DefaultTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String taskBeans = SpringContextUtils.getApplicationContext().getEnvironment()
                .getProperty("rainman.wf.all.task-beans",
                        String.class,
                        "defaultTaskConsumer,messageTaskConsumer,cmccoaTaskConsumer");

        List<String> taskBeanList = Lists.newArrayList(StringUtils.split(taskBeans, ","));

        for (String taskBean : taskBeanList) {
            if (!SpringContextUtils.getApplicationContext().containsBean(taskBean)) {
                continue;
            }

            Consumer consumer = SpringContextUtils.getBean(taskBean, Consumer.class);

            if (consumer == null) {
                continue;
            }

            consumer.accept(delegateTask);
        }
    }
}
