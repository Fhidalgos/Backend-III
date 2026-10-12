package cl.duoc.ms_cuentas.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransaccionConsumer {

    @KafkaListener(
            topics = "transacciones-completadas",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumir(String mensaje) {

        System.out.println(
                "KAFKA CONSUMER - Evento recibido:"
        );

        System.out.println(mensaje);
    }
}