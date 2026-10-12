package cl.duoc.bff_movil.dto;

import java.util.List;

public class ResumenMovilResponse {

    private String cliente;
    private List<CuentaMovilResponse> cuentas;
    private List<MovimientoMovilResponse> movimientos;

    public ResumenMovilResponse() {
    }

    public ResumenMovilResponse(
            String cliente,
            List<CuentaMovilResponse> cuentas,
            List<MovimientoMovilResponse> movimientos) {

        this.cliente = cliente;
        this.cuentas = cuentas;
        this.movimientos = movimientos;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<CuentaMovilResponse> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaMovilResponse> cuentas) {
        this.cuentas = cuentas;
    }

    public List<MovimientoMovilResponse> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoMovilResponse> movimientos) {
        this.movimientos = movimientos;
    }
}