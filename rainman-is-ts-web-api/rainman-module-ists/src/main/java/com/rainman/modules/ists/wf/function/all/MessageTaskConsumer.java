package com.rainman.modules.ists.wf.function.all;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 *
 */
@Component("messageTaskConsumer")
public class MessageTaskConsumer implements Consumer<DelegateTask> {
    @Override
    public void accept(DelegateTask delegateTask) {

    }
}
