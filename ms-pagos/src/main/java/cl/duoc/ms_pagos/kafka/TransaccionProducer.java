package cl.duoc.ms_pagos.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import cl.duoc.ms_pagos.event.TransaccionCompletadaEvent;

@Component
public class TransaccionProducer {

    private static final String TOPIC =
            "transacciones-completadas";

    private final KafkaTemplate<String, TransaccionCompletadaEvent>
            kafkaTemplate;

    public TransaccionProducer(
            KafkaTemplate<String, TransaccionCompletadaEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicar(
            TransaccionCompletadaEvent evento) {

        kafkaTemplate.send(
                TOPIC,
                evento
        );

        System.out.println(
                "KAFKA PRODUCER - Evento publicado: "
                + evento.getPagoId()
        );
    }
}