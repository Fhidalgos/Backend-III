package cl.duoc.bff_mobile.dto;

import java.math.BigDecimal;

public class CuentaMobileResponse {

    private Integer cuentaId;
    private String nombre;
    private BigDecimal saldo;

    public CuentaMobileResponse() {
    }

    public CuentaMobileResponse(Integer cuentaId,
                                String nombre,
                                BigDecimal saldo) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}