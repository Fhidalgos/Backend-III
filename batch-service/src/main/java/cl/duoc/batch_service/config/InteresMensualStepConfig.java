package cl.duoc.batch_service.config;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.exception.ErrorTemporalException;
import cl.duoc.batch_service.listener.BatchErrorListener;
import cl.duoc.batch_service.model.InteresMensual;
import cl.duoc.batch_service.processor.InteresMensualProcessor;

@Configuration
public class InteresMensualStepConfig {

    @Bean
    public Step interesMensualStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<InteresMensual> interesMensualReader,
            InteresMensualProcessor interesMensualProcessor,
            JdbcBatchItemWriter<InteresMensual> interesMensualWriter,
            BatchErrorListener batchErrorListener) {

        return new StepBuilder(
                "interesMensualStep",
                jobRepository
        )
                .<InteresMensual, InteresMensual>chunk(100)

                .reader(interesMensualReader)
                .processor(interesMensualProcessor)
                .writer(interesMensualWriter)

                .transactionManager(transactionManager)

                .faultTolerant()

                // Datos legacy inválidos
                .skip(DatoInvalidoException.class)
                .skip(FlatFileParseException.class)

                // Registros exactamente duplicados
                .skip(DuplicateKeyException.class)

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