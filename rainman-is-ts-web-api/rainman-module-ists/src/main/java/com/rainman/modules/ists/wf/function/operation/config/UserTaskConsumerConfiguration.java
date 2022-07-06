package com.rainman.modules.ists.wf.function.operation.config;

import com.rainman.modules.ists.wf.function.operation.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:operation")
public class UserTaskConsumerConfiguration {
    @Bean({"operation_user_task_20_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
