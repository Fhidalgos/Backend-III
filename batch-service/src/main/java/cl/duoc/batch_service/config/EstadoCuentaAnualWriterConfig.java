package cl.duoc.batch_service.config;

import javax.sql.DataSource;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.duoc.batch_service.model.EstadoCuentaAnual;

@Configuration
public class EstadoCuentaAnualWriterConfig {

    @Bean
    public JdbcBatchItemWriter<EstadoCuentaAnual> estadoCuentaAnualWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<EstadoCuentaAnual>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO estados_cuenta_anuales_procesados
                        (
                            cuenta_id,
                            fecha,
                            transaccion,
                            monto,
                            descripcion
                        )
                        VALUES
                        (
                            :cuentaId,
                            STR_TO_DATE(:fecha, '%Y-%m-%d'),
                            :transaccion,
                            :monto,
                            :descripcion
                        )
                        """)
                .beanMapped()
                .build();
    }
}