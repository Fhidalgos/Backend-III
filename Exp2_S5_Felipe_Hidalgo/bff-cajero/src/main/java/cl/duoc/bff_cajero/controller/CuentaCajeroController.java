package cl.duoc.bff_cajero.controller;

import cl.duoc.bff_cajero.dto.CuentaCajeroResponse;
import cl.duoc.bff_cajero.dto.RetiroRequest;
import cl.duoc.bff_cajero.dto.RetiroResponse;
import cl.duoc.bff_cajero.service.CuentaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cajero/cuentas")
public class CuentaCajeroController {

    private final CuentaService cuentaService;

    public CuentaCajeroController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/{cuentaId}/saldo")
    public ResponseEntity<CuentaCajeroResponse> obtenerSaldo(
            @PathVariable Integer cuentaId) {

        return cuentaService.obtenerSaldo(cuentaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cuentaId}/retiro")
    public ResponseEntity<RetiroResponse> retirar(
            @PathVariable Integer cuentaId,
            @Valid @RequestBody RetiroRequest request) {

        return cuentaService.retirar(
                        cuentaId,
                        request.getMonto()
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}