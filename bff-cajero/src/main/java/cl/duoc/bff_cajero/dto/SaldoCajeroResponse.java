package cl.duoc.bff_cajero.dto;

public class SaldoCajeroResponse {

    private Long cuentaId;
    private String tipoCuenta;
    private Double saldoDisponible;

    public SaldoCajeroResponse() {
    }

    public SaldoCajeroResponse(
            Long cuentaId,
            String tipoCuenta,
            Double saldoDisponible) {

        this.cuentaId = cuentaId;
        this.tipoCuenta = tipoCuenta;
        this.saldoDisponible = saldoDisponible;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(Double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}