package cl.duoc.ms_notificaciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Value("${app.notificaciones.simular-fallo:false}")
    private boolean simularFallo;

    public NotificacionService(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public void enviarNotificacion(String mensaje) {

        circuitBreakerFactory
                .create("servicioNotificaciones")
                .run(
                    () -> {
                        if (simularFallo) {
                            throw new RuntimeException(
                                    "Servicio externo de notificaciones no disponible");
                        }

                        System.out.println("Notificación generada correctamente");
                        return null;
                    },
                    throwable -> {
                        System.out.println(
                                "FALLBACK ACTIVADO: la notificación quedó pendiente");
                        return null;
                    }
                );
    }
}