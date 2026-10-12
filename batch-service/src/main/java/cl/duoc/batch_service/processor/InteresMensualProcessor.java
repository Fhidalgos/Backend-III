package cl.duoc.batch_service.processor;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import cl.duoc.batch_service.exception.DatoInvalidoException;
import cl.duoc.batch_service.model.InteresMensual;

@Component
public class InteresMensualProcessor
        implements ItemProcessor<InteresMensual, InteresMensual> {

    private static final double TASA_AHORRO = 0.01;
    private static final double TASA_PRESTAMO = 0.02;
    private static final double TASA_HIPOTECA = 0.015;

    @Override
    public InteresMensual process(InteresMensual interes) {

        if (interes.getCuentaId() == null || interes.getCuentaId() <= 0) {
            throw new DatoInvalidoException(
                    "ID de cuenta inválido"
            );
        }

        if (interes.getNombre() == null
                || interes.getNombre().isBlank()) {

            throw new DatoInvalidoException(
                    "Nombre vacío para la cuenta "
                            + interes.getCuentaId()
            );
        }

        if (interes.getSaldo() == null
                || !Double.isFinite(interes.getSaldo())
                || interes.getSaldo() <= 0) {

            throw new DatoInvalidoException(
                    "Saldo inválido para la cuenta "
                            + interes.getCuentaId()
            );
        }

        if (interes.getEdad() == null
                || interes.getEdad() < 18
                || interes.getEdad() > 99) {

            throw new DatoInvalidoException(
                    "Edad inválida para la cuenta "
                            + interes.getCuentaId()
            );
        }

        if (interes.getTipo() == null
                || interes.getTipo().isBlank()) {

            throw new DatoInvalidoException(
                    "Tipo de cuenta vacío para la cuenta "
                            + interes.getCuentaId()
            );
        }

        String tipoNormalizado =
                interes.getTipo().trim().toLowerCase();

        double tasa;

        switch (tipoNormalizado) {

            case "ahorro":
                tasa = TASA_AHORRO;
                break;

            case "prestamo":
                tasa = TASA_PRESTAMO;
                break;

            case "hipoteca":
                tasa = TASA_HIPOTECA;
                break;

            default:
                throw new DatoInvalidoException(
                        "Tipo de cuenta inválido: "
                                + interes.getTipo()
                );
        }

        double interesCalculado =
                interes.getSaldo() * tasa;

        double saldoConInteres =
                interes.getSaldo() + interesCalculado;

        interesCalculado =
                Math.round(interesCalculado * 100.0) / 100.0;

        saldoConInteres =
                Math.round(saldoConInteres * 100.0) / 100.0;

        interes.setNombre(interes.getNombre().trim());
        interes.setTipo(tipoNormalizado);
        interes.setInteresMensual(interesCalculado);
        interes.setSaldoConInteres(saldoConInteres);

        return interes;
    }
}