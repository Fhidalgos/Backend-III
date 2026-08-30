package cl.duoc.bankbatch.processor;

import cl.duoc.bankbatch.dto.CuentaAnualCsv;
import cl.duoc.bankbatch.model.CuentaAnual;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CuentaAnualProcessor implements ItemProcessor<CuentaAnualCsv, CuentaAnual> {

    @Override
    public CuentaAnual process(CuentaAnualCsv item) {

        try {

            // Validar ID de cuenta
            if (item.getCuentaId() == null || item.getCuentaId().isBlank()) {
                System.out.println("Registro anual omitido por ID de cuenta vacío.");
                return null;
            }

            Integer cuentaId = Integer.parseInt(item.getCuentaId());

            if (cuentaId <= 0) {
                System.out.println(
                        "Registro anual omitido por ID inválido: " + cuentaId
                );
                return null;
            }

            // Validar fecha
            if (item.getFecha() == null || item.getFecha().isBlank()) {
                System.out.println(
                        "Registro anual omitido por fecha vacía: " + cuentaId
                );
                return null;
            }

            LocalDate fecha = convertirFecha(item.getFecha());

            // Validar tipo de transacción
            if (item.getTransaccion() == null
                    || item.getTransaccion().isBlank()) {

                System.out.println(
                        "Registro anual omitido por transacción vacía: "
                                + cuentaId
                );
                return null;
            }

            String transaccion = item.getTransaccion()
                    .trim()
                    .toLowerCase();

            // Normalizar depósito con tilde
            if (transaccion.equals("depósito")) {
                transaccion = "deposito";
            }

            // Validar monto vacío
            if (item.getMonto() == null || item.getMonto().isBlank()) {

                System.out.println(
                        "Registro anual omitido por monto vacío: "
                                + cuentaId
                );
                return null;
            }

            BigDecimal monto = new BigDecimal(item.getMonto());

            // Validar monto
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {

                System.out.println(
                        "Registro anual omitido por monto inválido: "
                                + cuentaId + " - " + item.getMonto()
                );

                return null;
            }

            // Validar descripción
            if (item.getDescripcion() == null
                    || item.getDescripcion().isBlank()) {

                System.out.println(
                        "Registro anual omitido por descripción vacía: "
                                + cuentaId
                );

                return null;
            }

            CuentaAnual cuentaAnual = new CuentaAnual();

            cuentaAnual.setCuentaId(cuentaId);
            cuentaAnual.setFecha(fecha);
            cuentaAnual.setTransaccion(transaccion);
            cuentaAnual.setMonto(monto);
            cuentaAnual.setDescripcion(item.getDescripcion().trim());

            return cuentaAnual;

        } catch (NumberFormatException e) {

            System.out.println(
                    "Registro anual omitido por formato numérico inválido: "
                            + item.getCuentaId()
            );

            return null;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "Registro anual omitido por fecha inválida: "
                            + item.getFecha()
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