package cl.duoc.bankbatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchThreadConfig {

    @Value("${batch.threads:1}")
    private int cantidadHilos;

    @Bean
    public ThreadPoolTaskExecutor batchTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(cantidadHilos);
        executor.setMaxPoolSize(cantidadHilos);
        executor.setQueueCapacity(100);

        // Permite cerrar los hilos cuando quedan sin trabajo
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(1);

        executor.setThreadNamePrefix("batch-thread-");

        executor.initialize();

        return executor;
    }
}