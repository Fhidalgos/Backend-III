package cl.duoc.batch_service.config;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.exception.ErrorTemporalException;
import cl.duoc.batch_service.listener.BatchErrorListener;
import cl.duoc.batch_service.model.EstadoCuentaAnual;
import cl.duoc.batch_service.processor.EstadoCuentaAnualProcessor;

@Configuration
public class EstadoCuentaAnualStepConfig {

    @Bean
    public Step estadoCuentaAnualStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<EstadoCuentaAnual> estadoCuentaAnualReader,
            EstadoCuentaAnualProcessor estadoCuentaAnualProcessor,
            JdbcBatchItemWriter<EstadoCuentaAnual> estadoCuentaAnualWriter,
            BatchErrorListener batchErrorListener) {

        return new StepBuilder(
                "estadoCuentaAnualStep",
                jobRepository
        )
                .<EstadoCuentaAnual, EstadoCuentaAnual>chunk(100)

                .reader(estadoCuentaAnualReader)
                .processor(estadoCuentaAnualProcessor)
                .writer(estadoCuentaAnualWriter)

                .transactionManager(transactionManager)

                .faultTolerant()

                // Datos legacy inválidos
                .skip(DatoInvalidoException.class)
                .skip(FlatFileParseException.class)
                .skipLimit(
                        ConfiguracionErroresBatch
                                .LIMITE_REGISTROS_INVALIDOS
                )

                // Errores temporales
                .retry(ErrorTemporalException.class)
                .retry(TransientDataAccessException.class)
                .retryLimit(
                        ConfiguracionErroresBatch
                                .LIMITE_REINTENTOS
                )

                .skipListener(batchErrorListener)

                .build();
    }
}