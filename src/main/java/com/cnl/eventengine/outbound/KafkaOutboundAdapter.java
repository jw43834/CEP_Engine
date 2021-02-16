package com.cnl.eventengine.outbound;

import com.cnl.eventengine.config.KafkaProducerProperties;
import com.cnl.eventengine.domain.OccurrenceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.StringOrBytesSerializer;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class KafkaOutboundAdapter implements OutboundAdapter {
    private KafkaProducerProperties kafkaProducerProperties;
    private KafkaTemplate<String, OccurrenceEvent> kafkaTemplate;
    private String kafkaTopic;

    public KafkaOutboundAdapter(KafkaProducerProperties kafkaProducerProperties) {
        this.kafkaProducerProperties = kafkaProducerProperties;
    }

    @PostConstruct
    @Override
    public void initialize() {
        log.debug("Initialize Kafka Outbound Adapter");

        log.debug("Kafka Producer Properties : {}",kafkaProducerProperties);

        kafkaTopic = kafkaProducerProperties.getTopic();

        kafkaTemplate = createTemplate();
        kafkaTemplate.setDefaultTopic("Occurrence_Event");
    }

    @Override
    public void produce(OccurrenceEvent occurrenceEvent) {
        log.debug("Produce OccurrenceEvent :{}", occurrenceEvent);
        ListenableFuture<SendResult<String, OccurrenceEvent>> future = kafkaTemplate.send(kafkaTopic,occurrenceEvent);

        future.addCallback(new ListenableFutureCallback<SendResult<String, OccurrenceEvent>>() {

            @Override
            public void onSuccess(SendResult<String, OccurrenceEvent> result) {
                log.info("Sent message= [" + occurrenceEvent.toString() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Message 전달 오류 " + occurrenceEvent.toString() + "] due to : " + ex.getMessage());
            }
        });
    }

    private KafkaTemplate<String, OccurrenceEvent> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<String, OccurrenceEvent> pf =
                new DefaultKafkaProducerFactory<String, OccurrenceEvent>(senderProps);
        KafkaTemplate<String, OccurrenceEvent> template = new KafkaTemplate<>(pf);
        return template;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringOrBytesSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}
