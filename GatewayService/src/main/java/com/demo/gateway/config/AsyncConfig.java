package com.demo.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    public static final String ASYNC_EXECUTOR_NAME = "asyncTaskExecutor";

    @Value("${async.corePoolSize}")
    private int corePoolSize;

    @Value("${async.maxPoolSize}")
    private int maxPoolSize;

    @Value("${async.queueSize}")
    private int queueSize;

    @Bean(name = ASYNC_EXECUTOR_NAME)
    public Executor asyncTaskExecutor() {
        log.info("Initializing the Async thread pool");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueSize);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
