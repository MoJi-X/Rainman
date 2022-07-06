package com.rainman.modules.ists.wf.function.project_check.config;

import com.rainman.modules.ists.wf.function.project_check.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:project_check")
public class UserTaskConsumerConfiguration {
    @Bean({"project_check_user_task_20_consumer", "project_check_user_task_30_consumer", "project_check_user_task_40_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
