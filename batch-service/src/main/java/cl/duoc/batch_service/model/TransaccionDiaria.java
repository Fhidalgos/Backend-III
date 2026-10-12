package cl.duoc.batch_service.model;

public class TransaccionDiaria {

    private Long id;
    private String fecha;
    private Double monto;
    private String tipo;

    public TransaccionDiaria() {
    }

    public TransaccionDiaria(Long id, String fecha, Double monto, String tipo) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.tipo = tipo;
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

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "TransaccionDiaria{" +
                "id=" + id +
                ", fecha='" + fecha + '\'' +
                ", monto=" + monto +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}