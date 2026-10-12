package cl.duoc.batch_service.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstadoCuentaAnualJobConfig {

    @Bean
    public Job jobEstadosCuentaAnuales(
            JobRepository jobRepository,
            Step estadoCuentaAnualStep) {

        return new JobBuilder(
                "jobEstadosCuentaAnuales",
                jobRepository
        )
                .incrementer(new RunIdIncrementer())
                .start(estadoCuentaAnualStep)
                .build();
    }
}