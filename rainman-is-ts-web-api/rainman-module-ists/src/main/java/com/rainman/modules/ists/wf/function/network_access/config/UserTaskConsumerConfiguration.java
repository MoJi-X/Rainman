package com.rainman.modules.ists.wf.function.network_access.config;

import com.google.common.collect.Lists;
import com.rainman.modules.ists.wf.function.network_access.UserTask10Consumer;
import com.rainman.modules.ists.wf.function.network_access.UserTask20Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("userTaskConsumerConfiguration:network_access")
public class UserTaskConsumerConfiguration {
    @Bean({"network_access_user_task_10_consumer"})
    UserTask10Consumer userTask10Consumer() {
        return new UserTask10Consumer(Lists.newArrayList(
                "equipment_basic_info",
                "account_checklist",
                "service_port_checklist",
                "topology_map"));
    }

    @Bean({"network_access_user_task_20_consumer",
            "network_access_user_task_70_consumer"})
    UserTask20Consumer userTask20Consumer() {
        return new UserTask20Consumer(Lists.newArrayList());
    }

    @Bean({"network_access_user_task_30_consumer"})
    UserTask20Consumer userTask30Consumer() {
        return new UserTask20Consumer(Lists.newArrayList("assessment_report"));
    }

    @Bean({"network_access_user_task_40_consumer"})
    UserTask20Consumer userTask40Consumer() {
        return new UserTask20Consumer(Lists.newArrayList("proof_record",
                "test_report",
                "correction_report"));
    }

    @Bean({"network_access_user_task_50_consumer"})
    UserTask20Consumer userTask50Consumer() {
        return new UserTask20Consumer(Lists.newArrayList());
    }

    @Bean({"network_access_user_task_60_consumer"})
    UserTask20Consumer userTask60Consumer() {
        return new UserTask20Consumer(Lists.newArrayList("4A_template"));
    }
}
