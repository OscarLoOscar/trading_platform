package com.bc2403.bc_yahoo_finance.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@EnableAsync
public class SchedulingConfig {
  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    taskScheduler.setThreadNamePrefix("scheduled-task-");
    taskScheduler.setErrorHandler(new CustomErrorHandler());
    return taskScheduler;
  }
  
  @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

  private static class CustomErrorHandler implements ErrorHandler {
    // @SuppressWarnings("null")
    @Override
    public void handleError(Throwable t) {
      log.error("Unexpected error occurred in scheduled task", t);
    }
  }
}
