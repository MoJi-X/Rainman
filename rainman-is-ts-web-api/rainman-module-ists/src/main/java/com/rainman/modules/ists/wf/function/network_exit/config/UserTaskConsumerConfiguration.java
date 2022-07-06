package com.rainman.modules.ists.wf.function.network_exit.config;

import com.rainman.modules.ists.wf.function.network_exit.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:network_exit")
public class UserTaskConsumerConfiguration {
    @Bean({"network_exit_user_task_20_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
