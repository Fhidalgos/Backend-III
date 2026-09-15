package cl.duoc.bff_web.service;

import cl.duoc.bff_web.dto.CuentaWebResponse;
import cl.duoc.bff_web.model.Cuenta;
import cl.duoc.bff_web.repository.CuentaRepository;
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

    public List<CuentaWebResponse> obtenerTodasParaWeb() {

        List<Cuenta> cuentas = cuentaRepository.findAll();
        List<CuentaWebResponse> respuestas = new ArrayList<>();

        for (Cuenta cuenta : cuentas) {
            respuestas.add(convertirAWebResponse(cuenta));
        }

        return respuestas;
    }

    public Optional<CuentaWebResponse> obtenerPorIdParaWeb(Integer cuentaId) {

        Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

        if (cuenta.isPresent()) {
            return Optional.of(convertirAWebResponse(cuenta.get()));
        }

        return Optional.empty();
    }

    private CuentaWebResponse convertirAWebResponse(Cuenta cuenta) {

        return new CuentaWebResponse(
                cuenta.getCuentaId(),
                cuenta.getNombre(),
                cuenta.getSaldo(),
                cuenta.getEdad(),
                cuenta.getTipo()
        );
    }
}