package cl.duoc.bff_mobile.controller;

import cl.duoc.bff_mobile.dto.CuentaMobileResponse;
import cl.duoc.bff_mobile.service.CuentaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/cuentas")
public class CuentaMobileController {

    private final CuentaService cuentaService;

    public CuentaMobileController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaMobileResponse>> obtenerTodas() {
        return ResponseEntity.ok(
                cuentaService.obtenerTodasParaMobile()
        );
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaMobileResponse> obtenerPorId(
            @PathVariable Integer cuentaId) {

        return cuentaService.obtenerPorIdParaMobile(cuentaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}