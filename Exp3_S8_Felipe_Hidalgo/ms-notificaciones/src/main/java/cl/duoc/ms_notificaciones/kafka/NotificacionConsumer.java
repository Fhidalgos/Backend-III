package cl.duoc.ms_notificaciones.kafka;

import cl.duoc.ms_notificaciones.service.NotificacionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacionConsumer {

    private final NotificacionService notificacionService;

    public NotificacionConsumer(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @KafkaListener(
        topics = "${app.kafka.topic.resultados}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void recibirResultado(String mensaje) {

        System.out.println("========================================");
        System.out.println("MS-NOTIFICACIONES recibió un resultado");
        System.out.println("Evento recibido: " + mensaje);

        notificacionService.enviarNotificacion(mensaje);

        System.out.println("========================================");
    }
}