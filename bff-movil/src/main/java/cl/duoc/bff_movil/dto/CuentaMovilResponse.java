package cl.duoc.bff_movil.dto;

public class CuentaMovilResponse {

    private Long id;
    private String tipo;
    private Double saldo;

    public CuentaMovilResponse() {
    }

    public CuentaMovilResponse(
            Long id,
            String tipo,
            Double saldo) {

        this.id = id;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}