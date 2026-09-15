package cl.duoc.bff_cajero.dto;

import java.math.BigDecimal;

public class CuentaCajeroResponse {

    private Integer cuentaId;
    private BigDecimal saldo;

    public CuentaCajeroResponse() {
    }

    public CuentaCajeroResponse(Integer cuentaId, BigDecimal saldo) {
        this.cuentaId = cuentaId;
        this.saldo = saldo;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}