package cl.duoc.ms_pagos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.ms_pagos.dto.PagoRequest;
import cl.duoc.ms_pagos.dto.PagoResponse;
import cl.duoc.ms_pagos.event.TransaccionCompletadaEvent;
import cl.duoc.ms_pagos.kafka.TransaccionProducer;
import cl.duoc.ms_pagos.model.Pago;
import cl.duoc.ms_pagos.repository.PagoRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final TransaccionProducer transaccionProducer;

    public PagoService(
            PagoRepository pagoRepository,
            TransaccionProducer transaccionProducer) {

        this.pagoRepository = pagoRepository;
        this.transaccionProducer = transaccionProducer;
    }

    public List<PagoResponse> listar() {

        return pagoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public PagoResponse obtenerPorId(Long id) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pago no encontrado con id: " + id
                        )
                );

        return convertirAResponse(pago);
    }

    @Transactional
    public PagoResponse crear(PagoRequest request) {

        if (request.getCuentaOrigenId()
                .equals(request.getCuentaDestinoId())) {

            throw new IllegalArgumentException(
                    "La cuenta de origen y destino no pueden ser iguales"
            );
        }

        Pago pago = new Pago();

        pago.setCuentaOrigenId(
                request.getCuentaOrigenId()
        );

        pago.setCuentaDestinoId(
                request.getCuentaDestinoId()
        );

        pago.setTipoOperacion(
                request.getTipoOperacion()
        );

        pago.setMonto(
                request.getMonto()
        );

        pago.setEstado("COMPLETADA");
        pago.setFecha(LocalDateTime.now());

        Pago pagoGuardado =
                pagoRepository.save(pago);

        TransaccionCompletadaEvent evento =
                new TransaccionCompletadaEvent(
                        pagoGuardado.getId(),
                        pagoGuardado.getCuentaOrigenId(),
                        pagoGuardado.getCuentaDestinoId(),
                        pagoGuardado.getTipoOperacion(),
                        pagoGuardado.getMonto(),
                        pagoGuardado.getEstado(),
                        pagoGuardado.getFecha()
                );

        transaccionProducer.publicar(evento);

        return convertirAResponse(pagoGuardado);
    }

    private PagoResponse convertirAResponse(Pago pago) {

        return new PagoResponse(
                pago.getId(),
                pago.getCuentaOrigenId(),
                pago.getCuentaDestinoId(),
                pago.getTipoOperacion(),
                pago.getMonto(),
                pago.getEstado(),
                pago.getFecha()
        );
    }
}