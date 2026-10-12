package cl.duoc.batch_service.processor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.exception.ErrorTemporalException;
import cl.duoc.batch_service.model.TransaccionDiaria;

@Component
public class TransaccionDiariaProcessor
        implements ItemProcessor<TransaccionDiaria, TransaccionDiaria> {

    private static final Logger logger =
            LoggerFactory.getLogger(TransaccionDiariaProcessor.class);

    private static final List<DateTimeFormatter> FORMATOS_FECHA = List.of(
            DateTimeFormatter.ofPattern("uuuu/MM/dd")
                    .withResolverStyle(ResolverStyle.STRICT),

            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT),

            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT),

            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT)
    );

    private final AtomicInteger intentosErrorTemporal =
            new AtomicInteger(0);

    @Value("${batch.simular-error-temporal:false}")
    private boolean simularErrorTemporal;

    @Override
    public TransaccionDiaria process(TransaccionDiaria transaccion) {

        if (transaccion.getId() == null
                || transaccion.getId() <= 0) {

            throw new DatoInvalidoException(
                    "ID de transacción inválido"
            );
        }

        if (transaccion.getMonto() == null
                || !Double.isFinite(transaccion.getMonto())
                || transaccion.getMonto() <= 0) {

            throw new DatoInvalidoException(
                    "Monto inválido para la transacción ID "
                            + transaccion.getId()
            );
        }

        if (transaccion.getTipo() == null
                || transaccion.getTipo().isBlank()) {

            throw new DatoInvalidoException(
                    "Tipo de transacción vacío"
            );
        }

        String tipoNormalizado =
                transaccion.getTipo()
                        .trim()
                        .toLowerCase();

        if (!tipoNormalizado.equals("credito")
                && !tipoNormalizado.equals("debito")) {

            throw new DatoInvalidoException(
                    "Tipo de transacción inválido: "
                            + transaccion.getTipo()
            );
        }

        LocalDate fechaValida =
                convertirFecha(transaccion.getFecha());

        transaccion.setFecha(fechaValida.toString());
        transaccion.setTipo(tipoNormalizado);

        simularFalloTemporal(transaccion);

        return transaccion;
    }

    private void simularFalloTemporal(
            TransaccionDiaria transaccion) {

        if (!simularErrorTemporal
                || transaccion.getId() != 2L) {

            return;
        }

        int intento =
                intentosErrorTemporal.incrementAndGet();

        if (intento <= 2) {

            logger.warn(
                    "RETRY DEMO - fallo temporal simulado. Intento {} para transacción ID {}",
                    intento,
                    transaccion.getId()
            );

            throw new ErrorTemporalException(
                    "Fallo temporal simulado para demostrar retry"
            );
        }

        logger.info(
                "RETRY DEMO - transacción ID {} procesada correctamente en el intento {}",
                transaccion.getId(),
                intento
        );
    }

    private LocalDate convertirFecha(String fecha) {

        if (fecha == null || fecha.isBlank()) {

            throw new DatoInvalidoException(
                    "La fecha de la transacción está vacía"
            );
        }

        String fechaLimpia = fecha.trim();

        for (DateTimeFormatter formato : FORMATOS_FECHA) {

            try {
                return LocalDate.parse(
                        fechaLimpia,
                        formato
                );

            } catch (DateTimeParseException e) {
                // Intenta con el siguiente formato.
            }
        }

        throw new DatoInvalidoException(
                "Formato de fecha inválido: " + fecha
        );
    }
}