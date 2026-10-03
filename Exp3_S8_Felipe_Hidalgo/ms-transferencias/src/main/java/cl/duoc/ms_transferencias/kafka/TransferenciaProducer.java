package cl.duoc.ms_transferencias.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.solicitudes}")
    private String topicSolicitudes;

    public TransferenciaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarTransferencia(String mensaje) {

        kafkaTemplate.send(topicSolicitudes, mensaje);

        System.out.println("========================================");
        System.out.println("MS-TRANSFERENCIAS publicó un evento");
        System.out.println("Evento enviado: " + mensaje);
        System.out.println("========================================");
    }
}