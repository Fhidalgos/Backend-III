package cl.duoc.batch_service.config;

import javax.sql.DataSource;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.duoc.batch_service.model.InteresMensual;

@Configuration
public class InteresMensualWriterConfig {

    @Bean
    public JdbcBatchItemWriter<InteresMensual> interesMensualWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<InteresMensual>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO intereses_mensuales_procesados
                        (
                            cuenta_id,
                            nombre,
                            saldo,
                            edad,
                            tipo,
                            interes_mensual,
                            saldo_con_interes
                        )
                        VALUES
                        (
                            :cuentaId,
                            :nombre,
                            :saldo,
                            :edad,
                            :tipo,
                            :interesMensual,
                            :saldoConInteres
                        )
                        """)
                .beanMapped()
                .build();
    }
}