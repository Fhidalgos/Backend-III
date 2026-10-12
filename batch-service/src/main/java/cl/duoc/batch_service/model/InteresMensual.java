package cl.duoc.batch_service.model;

public class InteresMensual {

    private Long cuentaId;
    private String nombre;
    private Double saldo;
    private Integer edad;
    private String tipo;

    private Double interesMensual;
    private Double saldoConInteres;

    public InteresMensual() {
    }

    public InteresMensual(
            Long cuentaId,
            String nombre,
            Double saldo,
            Integer edad,
            String tipo) {

        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldo = saldo;
        this.edad = edad;
        this.tipo = tipo;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getInteresMensual() {
        return interesMensual;
    }

    public void setInteresMensual(Double interesMensual) {
        this.interesMensual = interesMensual;
    }

    public Double getSaldoConInteres() {
        return saldoConInteres;
    }

    public void setSaldoConInteres(Double saldoConInteres) {
        this.saldoConInteres = saldoConInteres;
    }

    @Override
    public String toString() {
        return "InteresMensual{" +
                "cuentaId=" + cuentaId +
                ", nombre='" + nombre + '\'' +
                ", saldo=" + saldo +
                ", edad=" + edad +
                ", tipo='" + tipo + '\'' +
                ", interesMensual=" + interesMensual +
                ", saldoConInteres=" + saldoConInteres +
                '}';
    }
}