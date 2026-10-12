package cl.duoc.batch_service.config;

import javax.sql.DataSource;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.duoc.batch_service.model.TransaccionDiaria;

@Configuration
public class TransaccionDiariaWriterConfig {

    @Bean
    public JdbcBatchItemWriter<TransaccionDiaria> transaccionDiariaWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<TransaccionDiaria>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO transacciones_diarias_procesadas
                        (
                            id,
                            fecha,
                            monto,
                            tipo
                        )
                        VALUES
                        (
                            :id,
                            STR_TO_DATE(:fecha, '%Y-%m-%d'),
                            :monto,
                            :tipo
                        )
                        ON DUPLICATE KEY UPDATE
                            fecha = STR_TO_DATE(:fecha, '%Y-%m-%d'),
                            monto = :monto,
                            tipo = :tipo
                        """)
                .beanMapped()
                .build();
    }
}