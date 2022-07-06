package com.rainman.modules.ists.wf.function.alter_report.config;

import com.rainman.modules.ists.wf.function.alter_report.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:alter_report")
public class UserTaskConsumerConfiguration {
    @Bean({"alter_report_user_task_20_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
