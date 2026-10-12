package cl.duoc.bff_movil.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.bff_movil.dto.CuentaMovilResponse;
import cl.duoc.bff_movil.dto.MovimientoMovilResponse;
import cl.duoc.bff_movil.dto.ResumenMovilResponse;

@Service
public class ResumenMovilService {

    public ResumenMovilResponse obtenerResumen() {

        List<CuentaMovilResponse> cuentas = List.of(
                new CuentaMovilResponse(
                        1L,
                        "Cuenta Corriente",
                        850000.0
                ),
                new CuentaMovilResponse(
                        2L,
                        "Cuenta Ahorro",
                        1250000.0
                )
        );

        List<MovimientoMovilResponse> movimientos = List.of(
                new MovimientoMovilResponse(
                        "2026-10-08",
                        "COMPRA",
                        45990.0
                ),
                new MovimientoMovilResponse(
                        "2026-10-07",
                        "DEPOSITO",
                        150000.0
                )
        );

        return new ResumenMovilResponse(
                "Cliente BFF Móvil",
                cuentas,
                movimientos
        );
    }
}