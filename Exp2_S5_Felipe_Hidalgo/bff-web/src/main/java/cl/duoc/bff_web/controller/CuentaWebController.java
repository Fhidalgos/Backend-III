package cl.duoc.bff_web.controller;

import cl.duoc.bff_web.dto.CuentaWebResponse;
import cl.duoc.bff_web.service.CuentaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/web/cuentas")
public class CuentaWebController {

    private final CuentaService cuentaService;

    public CuentaWebController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaWebResponse>> obtenerTodas() {
        return ResponseEntity.ok(
                cuentaService.obtenerTodasParaWeb()
        );
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaWebResponse> obtenerPorId(
            @PathVariable Integer cuentaId) {

        return cuentaService.obtenerPorIdParaWeb(cuentaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}