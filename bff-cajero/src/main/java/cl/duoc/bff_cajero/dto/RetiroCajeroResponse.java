package cl.duoc.bff_cajero.dto;

public class RetiroCajeroResponse {

    private Long cuentaId;
    private Double montoRetirado;
    private Double saldoRestante;
    private String estado;

    public RetiroCajeroResponse() {
    }

    public RetiroCajeroResponse(
            Long cuentaId,
            Double montoRetirado,
            Double saldoRestante,
            String estado) {

        this.cuentaId = cuentaId;
        this.montoRetirado = montoRetirado;
        this.saldoRestante = saldoRestante;
        this.estado = estado;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Double getMontoRetirado() {
        return montoRetirado;
    }

    public void setMontoRetirado(Double montoRetirado) {
        this.montoRetirado = montoRetirado;
    }

    public Double getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(Double saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}