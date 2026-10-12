package cl.duoc.ms_cuentas.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionCompletadaEvent {

    private Long pagoId;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private String tipoOperacion;
    private BigDecimal monto;
    private String estado;
    private LocalDateTime fecha;

    public TransaccionCompletadaEvent() {
    }

    public TransaccionCompletadaEvent(
            Long pagoId,
            Long cuentaOrigenId,
            Long cuentaDestinoId,
            String tipoOperacion,
            BigDecimal monto,
            String estado,
            LocalDateTime fecha) {

        this.pagoId = pagoId;
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.tipoOperacion = tipoOperacion;
        this.monto = monto;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public Long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(Long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}