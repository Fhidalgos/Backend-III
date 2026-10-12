package cl.duoc.batch_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchParallelConfig {

    @Bean
    public AsyncTaskExecutor batchTaskExecutor() {

        SimpleAsyncTaskExecutor executor =
                new SimpleAsyncTaskExecutor("batch-parallel-");

        executor.setConcurrencyLimit(4);

        return executor;
    }
}