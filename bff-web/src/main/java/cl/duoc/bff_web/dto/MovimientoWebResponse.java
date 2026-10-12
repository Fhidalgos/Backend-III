package cl.duoc.bff_web.dto;

public class MovimientoWebResponse {

    private Long id;
    private String fecha;
    private String tipo;
    private Double monto;
    private String descripcion;

    public MovimientoWebResponse() {
    }

    public MovimientoWebResponse(
            Long id,
            String fecha,
            String tipo,
            Double monto,
            String descripcion) {

        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}