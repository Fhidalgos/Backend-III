package cl.duoc.bff_cajero.service;

import org.springframework.stereotype.Service;

import cl.duoc.bff_cajero.dto.RetiroCajeroRequest;
import cl.duoc.bff_cajero.dto.RetiroCajeroResponse;
import cl.duoc.bff_cajero.dto.SaldoCajeroResponse;

@Service
public class CajeroService {

    private static final double SALDO_DISPONIBLE = 850000.0;

    public SaldoCajeroResponse consultarSaldo(Long cuentaId) {

        return new SaldoCajeroResponse(
                cuentaId,
                "Cuenta Corriente",
                SALDO_DISPONIBLE
        );
    }

    public RetiroCajeroResponse realizarRetiro(
            RetiroCajeroRequest request) {

        if (request.getMonto() > SALDO_DISPONIBLE) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente para realizar el retiro"
            );
        }

        double saldoRestante =
                SALDO_DISPONIBLE - request.getMonto();

        return new RetiroCajeroResponse(
                request.getCuentaId(),
                request.getMonto(),
                saldoRestante,
                "RETIRO APROBADO"
        );
    }
}