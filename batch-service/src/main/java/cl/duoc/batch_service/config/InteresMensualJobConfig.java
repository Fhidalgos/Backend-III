package cl.duoc.batch_service.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InteresMensualJobConfig {

    @Bean
    public Job jobCalculoInteresesMensuales(
            JobRepository jobRepository,
            Step interesMensualStep) {

        return new JobBuilder(
                "jobCalculoInteresesMensuales",
                jobRepository
        )
                .incrementer(new RunIdIncrementer())
                .start(interesMensualStep)
                .build();
    }
}