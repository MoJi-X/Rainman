package com.rainman.modules.ists.wf.function.ineed_check.config;

import com.rainman.modules.ists.wf.function.ineed_check.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:ineed_check")
public class UserTaskConsumerConfiguration {
    @Bean({
            "ineed_check_user_task_20_consumer",
            "ineed_check_user_task_30_consumer",
            "ineed_check_user_task_40_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
