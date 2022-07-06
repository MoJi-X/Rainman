package com.rainman.modules.ists.wf.function.accept.config;

import com.google.common.collect.Lists;
import com.rainman.modules.ists.wf.function.accept.UserTask10Consumer;
import com.rainman.modules.ists.wf.function.accept.UserTask20Consumer;
import com.rainman.modules.ists.wf.function.accept.UserTask30Consumer;
import com.rainman.modules.ists.wf.function.accept.UserTask40Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/1 22:32
 */
@Configuration
public class UserTaskConsumerConfiguration {

    @Bean("accept_user_task_30_consumer")
    UserTask30Consumer userTask30Consumer(){
        return new UserTask30Consumer(Lists.newArrayList("sys_test_report"));
    }
    @Bean("accept_user_task_40_consumer")
    UserTask40Consumer userTask40Consumer(){
        return new UserTask40Consumer(Lists.newArrayList("correction_report","proof_record","safety_accept_report"));
    }
    @Bean({"accept_user_task_10_consumer",
            "accept_user_task_60_consumer",
            "accept_user_task_70_consumer"
            })
    UserTask10Consumer userTask10Consumer(){
        return new UserTask10Consumer();
    }

    @Bean({"accept_user_task_20_consumer","accept_user_task_50_consumer","accept_user_task_80_consumer"})
    UserTask20Consumer userTask20Consumer(){return new UserTask20Consumer();}
}
