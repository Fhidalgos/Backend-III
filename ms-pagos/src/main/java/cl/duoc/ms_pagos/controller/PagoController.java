package cl.duoc.ms_pagos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_pagos.dto.PagoRequest;
import cl.duoc.ms_pagos.dto.PagoResponse;
import cl.duoc.ms_pagos.service.PagoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listar() {

        return ResponseEntity.ok(
                pagoService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pagoService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(
            @Valid @RequestBody PagoRequest request) {

        PagoResponse pagoCreado =
                pagoService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagoCreado);
    }
}