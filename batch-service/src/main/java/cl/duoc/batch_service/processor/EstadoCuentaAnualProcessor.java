package cl.duoc.batch_service.processor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.model.EstadoCuentaAnual;

@Component
public class EstadoCuentaAnualProcessor
        implements ItemProcessor<EstadoCuentaAnual, EstadoCuentaAnual> {

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

    @Override
    public EstadoCuentaAnual process(EstadoCuentaAnual estado) {

        if (estado.getCuentaId() == null || estado.getCuentaId() <= 0) {
            throw new DatoInvalidoException(
                    "ID de cuenta inválido"
            );
        }

        if (estado.getMonto() == null
                || !Double.isFinite(estado.getMonto())
                || estado.getMonto() <= 0) {

            throw new DatoInvalidoException(
                    "Monto inválido para la cuenta "
                            + estado.getCuentaId()
            );
        }

        if (estado.getTransaccion() == null
                || estado.getTransaccion().isBlank()) {

            throw new DatoInvalidoException(
                    "Tipo de transacción vacío para la cuenta "
                            + estado.getCuentaId()
            );
        }

        String transaccionNormalizada =
                estado.getTransaccion()
                        .trim()
                        .toLowerCase()
                        .replace("depósito", "deposito");

        if (!transaccionNormalizada.equals("compra")
                && !transaccionNormalizada.equals("retiro")
                && !transaccionNormalizada.equals("pago")
                && !transaccionNormalizada.equals("deposito")) {

            throw new DatoInvalidoException(
                    "Tipo de transacción inválido: "
                            + estado.getTransaccion()
            );
        }

        if (estado.getDescripcion() == null
                || estado.getDescripcion().isBlank()) {

            throw new DatoInvalidoException(
                    "Descripción vacía para la cuenta "
                            + estado.getCuentaId()
            );
        }

        LocalDate fechaValida =
                convertirFecha(estado.getFecha());

        estado.setFecha(fechaValida.toString());
        estado.setTransaccion(transaccionNormalizada);
        estado.setDescripcion(
                estado.getDescripcion().trim()
        );

        return estado;
    }

    private LocalDate convertirFecha(String fecha) {

        if (fecha == null || fecha.isBlank()) {
            throw new DatoInvalidoException(
                    "La fecha del estado de cuenta está vacía"
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