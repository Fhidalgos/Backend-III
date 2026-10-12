package cl.duoc.batch_service.model;

public class EstadoCuentaAnual {

    private Long cuentaId;
    private String fecha;
    private String transaccion;
    private Double monto;
    private String descripcion;

    public EstadoCuentaAnual() {
    }

    public EstadoCuentaAnual(
            Long cuentaId,
            String fecha,
            String transaccion,
            Double monto,
            String descripcion) {

        this.cuentaId = cuentaId;
        this.fecha = fecha;
        this.transaccion = transaccion;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
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

    @Override
    public String toString() {
        return "EstadoCuentaAnual{" +
                "cuentaId=" + cuentaId +
                ", fecha='" + fecha + '\'' +
                ", transaccion='" + transaccion + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}