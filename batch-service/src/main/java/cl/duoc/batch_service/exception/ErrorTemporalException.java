package cl.duoc.batch_service.exception;

public class ErrorTemporalException extends RuntimeException {

    public ErrorTemporalException(String mensaje) {
        super(mensaje);
    }

    public ErrorTemporalException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}