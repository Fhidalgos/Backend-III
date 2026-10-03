package cl.duoc.ms_transferencias.controller;

import cl.duoc.ms_transferencias.dto.TransferenciaRequest;
import cl.duoc.ms_transferencias.kafka.TransferenciaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaProducer transferenciaProducer;

    public TransferenciaController(TransferenciaProducer transferenciaProducer) {
        this.transferenciaProducer = transferenciaProducer;
    }

    @PostMapping
    public ResponseEntity<String> crearTransferencia(
            @RequestBody TransferenciaRequest request) {

        String mensaje =
                "TRANSFERENCIA_SOLICITADA"
                + " | cuentaOrigen=" + request.getCuentaOrigen()
                + " | cuentaDestino=" + request.getCuentaDestino()
                + " | monto=" + request.getMonto();

        transferenciaProducer.enviarTransferencia(mensaje);

        return ResponseEntity
                .accepted()
                .body("Transferencia recibida y enviada a procesamiento.");
    }
}