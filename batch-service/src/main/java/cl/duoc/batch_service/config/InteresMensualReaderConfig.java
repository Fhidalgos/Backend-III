package cl.duoc.batch_service.config;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import cl.duoc.batch_service.model.InteresMensual;

@Configuration
public class InteresMensualReaderConfig {

    @Bean
    public FlatFileItemReader<InteresMensual> interesMensualReader() {

        return new FlatFileItemReaderBuilder<InteresMensual>()
                .name("interesMensualReader")
                .resource(
                        new ClassPathResource(
                                "input/intereses_trimestrales.csv"
                        )
                )
                .encoding(StandardCharsets.UTF_8.name())
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(
                        "cuentaId",
                        "nombre",
                        "saldo",
                        "edad",
                        "tipo"
                )
                .targetType(InteresMensual.class)
                .build();
    }
}