package com.cnl.eventengine;

import com.cnl.eventengine.domain.CollectionEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.StringOrBytesSerializer;

import java.util.HashMap;
import java.util.Map;

public class CollectionEventProducer {
    @Test
    public void sendTest() throws InterruptedException {
        KafkaTemplate<String, CollectionEvent> template = createTemplate();

        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setDeviceId("a");
        collectionEvent.setOccDate("1234");
        collectionEvent.setValue(1);

        template.setDefaultTopic("Collection_Event");

        for(int i=0;i<10;i++){
            template.sendDefault("Collection_Event", collectionEvent);
            Thread.sleep(1000);
            System.out.println("Send Event");
        }
        template.flush();
    }

    private static KafkaTemplate<String, CollectionEvent> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<String, CollectionEvent> pf =
                new DefaultKafkaProducerFactory<String, CollectionEvent>(senderProps);
        KafkaTemplate<String, CollectionEvent> template = new KafkaTemplate<>(pf);
        return template;
    }

    private static Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringOrBytesSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}
