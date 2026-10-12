package cl.duoc.ms_cuentas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CuentaResponse {

    private Long id;
    private Long clienteId;
    private String tipoCuenta;
    private BigDecimal saldo;
    private String estado;
    private LocalDate fechaApertura;

    public CuentaResponse() {
    }

    public CuentaResponse(
            Long id,
            Long clienteId,
            String tipoCuenta,
            BigDecimal saldo,
            String estado,
            LocalDate fechaApertura) {

        this.id = id;
        this.clienteId = clienteId;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estado = estado;
        this.fechaApertura = fechaApertura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
}