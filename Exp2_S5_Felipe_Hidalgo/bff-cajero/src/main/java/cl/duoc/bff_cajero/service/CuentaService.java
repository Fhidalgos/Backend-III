package cl.duoc.bff_cajero.service;

import cl.duoc.bff_cajero.dto.CuentaCajeroResponse;
import cl.duoc.bff_cajero.dto.RetiroResponse;
import cl.duoc.bff_cajero.model.Cuenta;
import cl.duoc.bff_cajero.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Optional<CuentaCajeroResponse> obtenerSaldo(Integer cuentaId) {

        Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

        if (cuenta.isPresent()) {
            Cuenta cuentaEncontrada = cuenta.get();

            return Optional.of(
                    new CuentaCajeroResponse(
                            cuentaEncontrada.getCuentaId(),
                            cuentaEncontrada.getSaldo()
                    )
            );
        }

        return Optional.empty();
    }

    public Optional<RetiroResponse> retirar(
            Integer cuentaId,
            BigDecimal monto) {

        Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

        if (cuenta.isEmpty()) {
            return Optional.empty();
        }

        Cuenta cuentaEncontrada = cuenta.get();

        if (cuentaEncontrada.getSaldo().compareTo(monto) < 0) {
            return Optional.of(
                    new RetiroResponse(
                            cuentaEncontrada.getCuentaId(),
                            BigDecimal.ZERO,
                            cuentaEncontrada.getSaldo(),
                            "Saldo insuficiente"
                    )
            );
        }

        BigDecimal nuevoSaldo =
                cuentaEncontrada.getSaldo().subtract(monto);

        cuentaEncontrada.setSaldo(nuevoSaldo);

        cuentaRepository.save(cuentaEncontrada);

        return Optional.of(
                new RetiroResponse(
                        cuentaEncontrada.getCuentaId(),
                        monto,
                        nuevoSaldo,
                        "Retiro realizado correctamente"
                )
        );
    }
}