package cl.duoc.batch_service.config;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import cl.duoc.batch_service.model.TransaccionDiaria;

@Configuration
public class TransaccionDiariaReaderConfig {

    @Bean
    public SynchronizedItemStreamReader<TransaccionDiaria>
            transaccionDiariaReader() {

        FlatFileItemReader<TransaccionDiaria> reader =
                new FlatFileItemReaderBuilder<TransaccionDiaria>()
                        .name("transaccionDiariaReader")
                        .resource(
                                new ClassPathResource(
                                        "input/movimientos_financieros_diarios.csv"
                                )
                        )
                        .encoding(StandardCharsets.UTF_8.name())
                        .linesToSkip(1)
                        .delimited()
                        .delimiter(",")
                        .names(
                                "id",
                                "fecha",
                                "monto",
                                "tipo"
                        )
                        .targetType(TransaccionDiaria.class)
                        .build();

        return new SynchronizedItemStreamReader<>(reader);
    }
}