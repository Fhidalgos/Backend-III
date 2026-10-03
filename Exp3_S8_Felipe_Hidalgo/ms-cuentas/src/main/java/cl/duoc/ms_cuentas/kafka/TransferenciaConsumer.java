package cl.duoc.ms_cuentas.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.resultados}")
    private String topicResultados;

    public TransferenciaConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
        topics = "${app.kafka.topic.solicitudes}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void procesarTransferencia(String mensaje) {

        System.out.println("========================================");
        System.out.println("MS-CUENTAS recibió una transferencia");
        System.out.println("Evento recibido: " + mensaje);

        String resultado = "TRANSFERENCIA_PROCESADA | " + mensaje;

        kafkaTemplate.send(topicResultados, resultado);

        System.out.println("Resultado publicado: " + resultado);
        System.out.println("========================================");
    }
}