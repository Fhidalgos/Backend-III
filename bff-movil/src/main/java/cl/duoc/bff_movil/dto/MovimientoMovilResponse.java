package cl.duoc.bff_movil.dto;

public class MovimientoMovilResponse {

    private String fecha;
    private String tipo;
    private Double monto;

    public MovimientoMovilResponse() {
    }

    public MovimientoMovilResponse(
            String fecha,
            String tipo,
            Double monto) {

        this.fecha = fecha;
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}