package cl.duoc.bankbatch.config;

import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class TransaccionSkipPolicy implements SkipPolicy {

    private static final long LIMITE_SKIPS = 10;

    @Override
    public boolean shouldSkip(Throwable throwable, long skipCount) {

        // Spring Batch puede consultar la política con un contador negativo
        // solamente para comprobar si el tipo de error es saltable.
        if (skipCount < 0) {
            return throwable instanceof FlatFileParseException;
        }

        // Se permiten hasta 10 errores de lectura del archivo CSV.
        return throwable instanceof FlatFileParseException
                && skipCount < LIMITE_SKIPS;
    }
}