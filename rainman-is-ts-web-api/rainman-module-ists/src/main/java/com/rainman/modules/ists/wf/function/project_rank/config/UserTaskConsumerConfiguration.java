package com.rainman.modules.ists.wf.function.project_rank.config;

import com.rainman.modules.ists.wf.function.project_rank.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:project_rank")
public class UserTaskConsumerConfiguration {
    @Bean({"project_rank_user_task_20_consumer", "project_rank_user_task_30_consumer", "project_rank_user_task_40_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
