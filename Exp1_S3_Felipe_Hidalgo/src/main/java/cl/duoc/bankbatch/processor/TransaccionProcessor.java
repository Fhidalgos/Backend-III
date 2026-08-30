package cl.duoc.bankbatch.processor;

import cl.duoc.bankbatch.dto.TransaccionCsv;
import cl.duoc.bankbatch.model.Transaccion;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransaccionProcessor implements ItemProcessor<TransaccionCsv, Transaccion> {

    @Override
    public Transaccion process(TransaccionCsv item) {

        try {

            // Validar campos vacíos
            if (item.getId() == null || item.getId().isBlank()
                    || item.getFecha() == null || item.getFecha().isBlank()
                    || item.getMonto() == null || item.getMonto().isBlank()
                    || item.getTipo() == null || item.getTipo().isBlank()) {

                System.out.println("Transacción omitida por campos vacíos.");
                return null;
            }

            Integer id = Integer.parseInt(item.getId());

            LocalDate fecha = convertirFecha(item.getFecha());

            BigDecimal monto = new BigDecimal(item.getMonto());

            String tipo = item.getTipo();

            // Validar monto
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {

                System.out.println(
                        "Transacción omitida por monto inválido: "
                                + item.getMonto()
                );

                return null;
            }

            // Validar tipo de transacción
            if (!tipo.equalsIgnoreCase("debito")
                    && !tipo.equalsIgnoreCase("credito")) {

                System.out.println(
                        "Transacción omitida por tipo inválido: "
                                + tipo
                );

                return null;
            }

            return new Transaccion(
                    id,
                    fecha,
                    monto,
                    tipo.toLowerCase()
            );

        } catch (NumberFormatException | DateTimeParseException e) {

            System.out.println(
                    "Transacción omitida por formato inválido: "
                            + item.getId() + ", "
                            + item.getFecha() + ", "
                            + item.getMonto()
            );

            return null;
        }
    }

    private LocalDate convertirFecha(String fecha) {

        DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        };

        for (DateTimeFormatter formato : formatos) {

            try {
                return LocalDate.parse(fecha, formato);

            } catch (DateTimeParseException e) {
                // Intenta con el siguiente formato
            }
        }

        throw new DateTimeParseException(
                "Formato de fecha inválido",
                fecha,
                0
        );
    }
}