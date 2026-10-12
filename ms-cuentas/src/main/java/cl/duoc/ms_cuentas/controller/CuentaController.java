package cl.duoc.ms_cuentas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_cuentas.dto.CuentaRequest;
import cl.duoc.ms_cuentas.dto.CuentaResponse;
import cl.duoc.ms_cuentas.service.CuentaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listar() {

        return ResponseEntity.ok(
                cuentaService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cuentaService.obtenerPorId(id)
        );
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponse>> listarPorCliente(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                cuentaService.listarPorCliente(clienteId)
        );
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(
            @Valid @RequestBody CuentaRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {

        CuentaResponse cuentaCreada =
                cuentaService.crear(
                        request,
                        authorizationHeader
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cuentaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequest request) {

        return ResponseEntity.ok(
                cuentaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        cuentaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}