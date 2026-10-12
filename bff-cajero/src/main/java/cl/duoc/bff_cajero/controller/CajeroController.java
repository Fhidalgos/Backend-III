package cl.duoc.bff_cajero.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bff_cajero.dto.RetiroCajeroRequest;
import cl.duoc.bff_cajero.dto.RetiroCajeroResponse;
import cl.duoc.bff_cajero.dto.SaldoCajeroResponse;
import cl.duoc.bff_cajero.service.CajeroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cajero")
public class CajeroController {

    private final CajeroService cajeroService;

    public CajeroController(CajeroService cajeroService) {
        this.cajeroService = cajeroService;
    }

    @GetMapping("/saldo/{cuentaId}")
    public ResponseEntity<SaldoCajeroResponse> consultarSaldo(
            @PathVariable Long cuentaId) {

        return ResponseEntity.ok(
                cajeroService.consultarSaldo(cuentaId)
        );
    }

    @PostMapping("/retiro")
    public ResponseEntity<RetiroCajeroResponse> realizarRetiro(
            @Valid @RequestBody RetiroCajeroRequest request) {

        return ResponseEntity.ok(
                cajeroService.realizarRetiro(request)
        );
    }
}