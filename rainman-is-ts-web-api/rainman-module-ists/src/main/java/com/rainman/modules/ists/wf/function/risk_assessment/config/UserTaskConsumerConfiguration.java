package com.rainman.modules.ists.wf.function.risk_assessment.config;

import com.rainman.modules.ists.wf.function.risk_assessment.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:risk_assessment")
public class UserTaskConsumerConfiguration {
    @Bean({"risk_assessment_user_task_20_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer();
    }
}
