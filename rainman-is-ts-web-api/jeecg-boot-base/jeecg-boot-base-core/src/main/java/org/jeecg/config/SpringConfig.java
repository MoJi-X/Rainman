package org.jeecg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class SpringConfig {
    @ConfigurationProperties(prefix = "rainman.task-executor")
    @Bean({"org.springframework.core.task.AsyncListenableTaskExecutor", "taskExecutor"})
    public AsyncListenableTaskExecutor taskExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize * 2 + 1);
        threadPoolTaskExecutor.setKeepAliveSeconds(30);
        threadPoolTaskExecutor.setThreadNamePrefix("taskExecutor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return threadPoolTaskExecutor;
    }

    @ConfigurationProperties(prefix = "rainman.slow-task-executor")
    @Bean({"org.springframework.core.task.AsyncListenableTaskExecutor.slow", "slowTaskExecutor"})
    public AsyncListenableTaskExecutor taskExecutorSlow() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize + 1);
        threadPoolTaskExecutor.setKeepAliveSeconds(30);
        threadPoolTaskExecutor.setThreadNamePrefix("slowTaskExecutor-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return threadPoolTaskExecutor;
    }
}
