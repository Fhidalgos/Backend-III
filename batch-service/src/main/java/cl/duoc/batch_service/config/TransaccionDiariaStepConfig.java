package cl.duoc.batch_service.config;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.exception.ErrorTemporalException;
import cl.duoc.batch_service.listener.BatchErrorListener;
import cl.duoc.batch_service.model.TransaccionDiaria;
import cl.duoc.batch_service.processor.TransaccionDiariaProcessor;

@Configuration
public class TransaccionDiariaStepConfig {

    @Bean
    public Step transaccionDiariaStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            SynchronizedItemStreamReader<TransaccionDiaria> transaccionDiariaReader,
            TransaccionDiariaProcessor transaccionDiariaProcessor,
            JdbcBatchItemWriter<TransaccionDiaria> transaccionDiariaWriter,
            BatchErrorListener batchErrorListener,
            AsyncTaskExecutor batchTaskExecutor) {

        return new StepBuilder(
                "transaccionDiariaStep",
                jobRepository
        )
                .<TransaccionDiaria, TransaccionDiaria>chunk(100)

                .reader(transaccionDiariaReader)
                .processor(transaccionDiariaProcessor)
                .writer(transaccionDiariaWriter)

                .transactionManager(transactionManager)

                // Procesamiento concurrente
                .taskExecutor(batchTaskExecutor)

                .faultTolerant()

                // Registros inválidos
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