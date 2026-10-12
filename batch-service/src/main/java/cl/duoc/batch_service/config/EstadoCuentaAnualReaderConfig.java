package cl.duoc.batch_service.config;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import cl.duoc.batch_service.model.EstadoCuentaAnual;

@Configuration
public class EstadoCuentaAnualReaderConfig {

    @Bean
    public FlatFileItemReader<EstadoCuentaAnual> estadoCuentaAnualReader() {

        return new FlatFileItemReaderBuilder<EstadoCuentaAnual>()
                .name("estadoCuentaAnualReader")
                .resource(
                        new ClassPathResource(
                                "input/estados_financieros_anuales.csv"
                        )
                )
                .encoding(StandardCharsets.UTF_8.name())
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                        "cuentaId",
                        "fecha",
                        "transaccion",
                        "monto",
                        "descripcion"
                )
                .targetType(EstadoCuentaAnual.class)
                .build();
    }
}