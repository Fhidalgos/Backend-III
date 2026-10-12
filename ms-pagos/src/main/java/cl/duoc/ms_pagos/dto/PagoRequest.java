package cl.duoc.ms_pagos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagoRequest {

    @NotNull(message = "La cuenta de origen es obligatoria")
    @Positive(message = "La cuenta de origen debe ser mayor que 0")
    private Long cuentaOrigenId;

    @NotNull(message = "La cuenta de destino es obligatoria")
    @Positive(message = "La cuenta de destino debe ser mayor que 0")
    private Long cuentaDestinoId;

    @NotBlank(message = "El tipo de operación es obligatorio")
    private String tipoOperacion;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

    public PagoRequest() {
    }

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public Long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(Long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}