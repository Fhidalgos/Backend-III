package cl.duoc.bff_cajero.dto;

import java.math.BigDecimal;

public class RetiroResponse {

    private Integer cuentaId;
    private BigDecimal montoRetirado;
    private BigDecimal saldoActual;
    private String mensaje;

    public RetiroResponse() {
    }

    public RetiroResponse(Integer cuentaId,
                          BigDecimal montoRetirado,
                          BigDecimal saldoActual,
                          String mensaje) {
        this.cuentaId = cuentaId;
        this.montoRetirado = montoRetirado;
        this.saldoActual = saldoActual;
        this.mensaje = mensaje;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getMontoRetirado() {
        return montoRetirado;
    }

    public void setMontoRetirado(BigDecimal montoRetirado) {
        this.montoRetirado = montoRetirado;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}