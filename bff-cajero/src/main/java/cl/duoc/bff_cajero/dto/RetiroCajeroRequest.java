package cl.duoc.bff_cajero.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RetiroCajeroRequest {

    @NotNull(message = "La cuenta es obligatoria")
    @Positive(message = "El ID de la cuenta debe ser mayor que cero")
    private Long cuentaId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    private Double monto;

    public RetiroCajeroRequest() {
    }

    public RetiroCajeroRequest(
            Long cuentaId,
            Double monto) {

        this.cuentaId = cuentaId;
        this.monto = monto;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
