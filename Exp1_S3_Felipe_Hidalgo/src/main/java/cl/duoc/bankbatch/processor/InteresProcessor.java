package cl.duoc.bankbatch.processor;

import cl.duoc.bankbatch.dto.InteresCsv;
import cl.duoc.bankbatch.model.Interes;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InteresProcessor implements ItemProcessor<InteresCsv, Interes> {

    @Override
    public Interes process(InteresCsv item) {

        try {

            // Validar ID de cuenta
            if (item.getCuentaId() == null || item.getCuentaId().isBlank()) {
                System.out.println("Cuenta omitida por ID vacío.");
                return null;
            }

            Integer cuentaId = Integer.parseInt(item.getCuentaId());

            if (cuentaId <= 0) {
                System.out.println(
                        "Cuenta omitida por ID inválido: " + cuentaId
                );
                return null;
            }

            // Validar nombre
            if (item.getNombre() == null
                    || item.getNombre().isBlank()
                    || item.getNombre().equalsIgnoreCase("Unknown")) {

                System.out.println(
                        "Cuenta omitida por nombre inválido: " + cuentaId
                );
                return null;
            }

            // Validar saldo vacío
            if (item.getSaldo() == null || item.getSaldo().isBlank()) {
                System.out.println(
                        "Cuenta omitida por saldo vacío: " + cuentaId
                );
                return null;
            }

            BigDecimal saldo = new BigDecimal(item.getSaldo());

            // Validar saldo
            if (saldo.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(
                        "Cuenta omitida por saldo inválido: " + cuentaId
                );
                return null;
            }

            // Validar edad vacía
            if (item.getEdad() == null || item.getEdad().isBlank()) {
                System.out.println(
                        "Cuenta omitida por edad vacía: " + cuentaId
                );
                return null;
            }

            Integer edad = Integer.parseInt(item.getEdad());

            // Se aceptan edades entre 18 y 100 años
            if (edad < 18 || edad > 100) {
                System.out.println(
                        "Cuenta omitida por edad inválida: "
                                + cuentaId + " - Edad: " + edad
                );
                return null;
            }

            // Validar tipo de cuenta
            if (item.getTipo() == null || item.getTipo().isBlank()) {
                System.out.println(
                        "Cuenta omitida por tipo vacío: " + cuentaId
                );
                return null;
            }

            String tipo = item.getTipo().trim().toLowerCase();

            if (!tipo.equals("ahorro") && !tipo.equals("prestamo")) {
                System.out.println(
                        "Cuenta omitida por tipo inválido: "
                                + cuentaId + " - Tipo: " + tipo
                );
                return null;
            }

            // Definir tasa según el tipo de cuenta
            BigDecimal tasa;

            if (tipo.equals("ahorro")) {
                tasa = new BigDecimal("0.01");
            } else {
                tasa = new BigDecimal("0.02");
            }

            // Calcular interés
            BigDecimal interesCalculado = saldo
                    .multiply(tasa)
                    .setScale(2, RoundingMode.HALF_UP);

            // Calcular saldo final
            BigDecimal saldoFinal = saldo
                    .add(interesCalculado)
                    .setScale(2, RoundingMode.HALF_UP);

            Interes interes = new Interes();

            interes.setCuentaId(cuentaId);
            interes.setNombre(item.getNombre().trim());
            interes.setSaldo(saldo);
            interes.setEdad(edad);
            interes.setTipo(tipo);
            interes.setInteresCalculado(interesCalculado);
            interes.setSaldoFinal(saldoFinal);

            return interes;

        } catch (NumberFormatException e) {

            System.out.println(
                    "Cuenta omitida por formato numérico inválido: "
                            + item.getCuentaId()
            );

            return null;
        }
    }
}