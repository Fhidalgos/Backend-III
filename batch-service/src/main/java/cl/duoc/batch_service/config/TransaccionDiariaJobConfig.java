package cl.duoc.batch_service.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransaccionDiariaJobConfig {

    @Bean
    public Job jobTransaccionesDiarias(
            JobRepository jobRepository,
            Step transaccionDiariaStep) {

        return new JobBuilder(
                "jobTransaccionesDiarias",
                jobRepository
        )
                .incrementer(new RunIdIncrementer())
                .start(transaccionDiariaStep)
                .build();
    }
}