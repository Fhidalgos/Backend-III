package cl.duoc.bff_movil.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bff_movil.dto.ResumenMovilResponse;
import cl.duoc.bff_movil.service.ResumenMovilService;

@RestController
@RequestMapping("/api/movil")
public class ResumenMovilController {

    private final ResumenMovilService resumenMovilService;

    public ResumenMovilController(
            ResumenMovilService resumenMovilService) {

        this.resumenMovilService = resumenMovilService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenMovilResponse> obtenerResumen() {

        return ResponseEntity.ok(
                resumenMovilService.obtenerResumen()
        );
    }
}