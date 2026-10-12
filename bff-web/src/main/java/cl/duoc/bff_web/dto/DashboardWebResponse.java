package cl.duoc.bff_web.dto;

import java.util.List;

public class DashboardWebResponse {

    private String cliente;
    private List<CuentaWebResponse> cuentas;
    private List<MovimientoWebResponse> movimientos;

    public DashboardWebResponse() {
    }

    public DashboardWebResponse(
            String cliente,
            List<CuentaWebResponse> cuentas,
            List<MovimientoWebResponse> movimientos) {

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

    public List<CuentaWebResponse> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaWebResponse> cuentas) {
        this.cuentas = cuentas;
    }

    public List<MovimientoWebResponse> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoWebResponse> movimientos) {
        this.movimientos = movimientos;
    }
}
