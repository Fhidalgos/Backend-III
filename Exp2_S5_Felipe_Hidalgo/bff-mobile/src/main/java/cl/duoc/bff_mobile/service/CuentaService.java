package cl.duoc.bff_mobile.service;

import cl.duoc.bff_mobile.dto.CuentaMobileResponse;
import cl.duoc.bff_mobile.model.Cuenta;
import cl.duoc.bff_mobile.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<CuentaMobileResponse> obtenerTodasParaMobile() {

        List<Cuenta> cuentas = cuentaRepository.findAll();
        List<CuentaMobileResponse> respuestas = new ArrayList<>();

        for (Cuenta cuenta : cuentas) {
            respuestas.add(convertirAMobileResponse(cuenta));
        }

        return respuestas;
    }

    public Optional<CuentaMobileResponse> obtenerPorIdParaMobile(
            Integer cuentaId) {

        Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

        if (cuenta.isPresent()) {
            return Optional.of(
                    convertirAMobileResponse(cuenta.get())
            );
        }

        return Optional.empty();
    }

    private CuentaMobileResponse convertirAMobileResponse(Cuenta cuenta) {

        return new CuentaMobileResponse(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getSaldo()
        );
    }
}