package cl.duoc.bff_web.dto;

public class CuentaWebResponse {

    private Long id;
    private String tipoCuenta;
    private Double saldo;
    private String estado;
    private String fechaApertura;

    public CuentaWebResponse() {
    }

    public CuentaWebResponse(
            Long id,
            String tipoCuenta,
            Double saldo,
            String estado,
            String fechaApertura) {

        this.id = id;
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

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
}