package cl.duoc.bff_web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.bff_web.dto.CuentaWebResponse;
import cl.duoc.bff_web.dto.DashboardWebResponse;
import cl.duoc.bff_web.dto.MovimientoWebResponse;

@Service
public class DashboardWebService {

    public DashboardWebResponse obtenerDashboard() {

        List<CuentaWebResponse> cuentas = List.of(
                new CuentaWebResponse(
                        1L,
                        "Cuenta Corriente",
                        850000.0,
                        "ACTIVA",
                        "2024-01-15"
                ),
                new CuentaWebResponse(
                        2L,
                        "Cuenta Ahorro",
                        1250000.0,
                        "ACTIVA",
                        "2023-08-10"
                )
        );

        List<MovimientoWebResponse> movimientos = List.of(
                new MovimientoWebResponse(
                        1L,
                        "2026-10-08",
                        "COMPRA",
                        45990.0,
                        "Compra comercio electrónico"
                ),
                new MovimientoWebResponse(
                        2L,
                        "2026-10-07",
                        "DEPOSITO",
                        150000.0,
                        "Depósito en cuenta"
                )
        );

        return new DashboardWebResponse(
                "Cliente BFF Web",
                cuentas,
                movimientos
        );
    }
}