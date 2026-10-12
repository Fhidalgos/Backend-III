package cl.duoc.ms_cuentas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.ms_cuentas.client.ClienteClient;
import cl.duoc.ms_cuentas.dto.CuentaRequest;
import cl.duoc.ms_cuentas.dto.CuentaResponse;
import cl.duoc.ms_cuentas.event.TransaccionCompletadaEvent;
import cl.duoc.ms_cuentas.model.Cuenta;
import cl.duoc.ms_cuentas.repository.CuentaRepository;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteClient clienteClient;

    public CuentaService(
            CuentaRepository cuentaRepository,
            ClienteClient clienteClient) {

        this.cuentaRepository = cuentaRepository;
        this.clienteClient = clienteClient;
    }

    public List<CuentaResponse> listar() {

        return cuentaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public CuentaResponse obtenerPorId(Long id) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cuenta no encontrada con id: " + id
                        )
                );

        return convertirAResponse(cuenta);
    }

    public List<CuentaResponse> listarPorCliente(Long clienteId) {

        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public CuentaResponse crear(
            CuentaRequest request,
            String authorizationHeader) {

        boolean clienteExiste = clienteClient.existeCliente(
                request.getClienteId(),
                authorizationHeader
        );

        if (!clienteExiste) {
            throw new IllegalArgumentException(
                    "El cliente no existe o ms-clientes no está disponible"
            );
        }

        Cuenta cuenta = new Cuenta();

        cuenta.setClienteId(request.getClienteId());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldo(request.getSaldo());
        cuenta.setEstado("ACTIVA");
        cuenta.setFechaApertura(LocalDate.now());

        Cuenta cuentaGuardada =
                cuentaRepository.save(cuenta);

        return convertirAResponse(cuentaGuardada);
    }

    public CuentaResponse actualizar(
            Long id,
            CuentaRequest request) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cuenta no encontrada con id: " + id
                        )
                );

        cuenta.setClienteId(request.getClienteId());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldo(request.getSaldo());

        Cuenta cuentaActualizada =
                cuentaRepository.save(cuenta);

        return convertirAResponse(cuentaActualizada);
    }

    public void eliminar(Long id) {

        if (!cuentaRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Cuenta no encontrada con id: " + id
            );
        }

        cuentaRepository.deleteById(id);
    }

    @Transactional
    public void procesarTransaccion(
            TransaccionCompletadaEvent evento) {

        Cuenta cuentaOrigen = cuentaRepository
                .findById(evento.getCuentaOrigenId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cuenta de origen no encontrada: "
                                        + evento.getCuentaOrigenId()
                        )
                );

        Cuenta cuentaDestino = cuentaRepository
                .findById(evento.getCuentaDestinoId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cuenta de destino no encontrada: "
                                        + evento.getCuentaDestinoId()
                        )
                );

        BigDecimal monto = evento.getMonto();

        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente en la cuenta de origen"
            );
        }

        cuentaOrigen.setSaldo(
                cuentaOrigen.getSaldo().subtract(monto)
        );

        cuentaDestino.setSaldo(
                cuentaDestino.getSaldo().add(monto)
        );

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        System.out.println(
                "KAFKA - Transferencia aplicada correctamente. Pago: "
                        + evento.getPagoId()
        );

        System.out.println(
                "Cuenta origen "
                        + cuentaOrigen.getId()
                        + " nuevo saldo: "
                        + cuentaOrigen.getSaldo()
        );

        System.out.println(
                "Cuenta destino "
                        + cuentaDestino.getId()
                        + " nuevo saldo: "
                        + cuentaDestino.getSaldo()
        );
    }

    private CuentaResponse convertirAResponse(
            Cuenta cuenta) {

        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getClienteId(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldo(),
                cuenta.getEstado(),
                cuenta.getFechaApertura()
        );
    }
}