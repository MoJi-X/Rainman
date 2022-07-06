package com.rainman.modules.ists.wf.function.disposal.config;

import com.rainman.modules.ists.wf.function.disposal.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:disposal")
public class UserTaskConsumerConfiguration {
    @Bean({"disposal_user_task_20_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
