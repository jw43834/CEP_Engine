package com.cnl.eventengine;

import com.cnl.eventengine.domain.OccurrenceEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class OccurenceEventConsumer {
    public static void main(String[] args){
        ContainerProperties containerProps = new ContainerProperties("Occurrence_Event");
        final CountDownLatch latch = new CountDownLatch(4);
        containerProps.setMessageListener(new MessageListener<String, OccurrenceEvent>() {

            @Override
            public void onMessage(ConsumerRecord<String, OccurrenceEvent> message) {
                System.out.println("received: " + message.value());
                latch.countDown();
            }

        });

        KafkaMessageListenerContainer<String, OccurrenceEvent> container = createContainer(containerProps);
        container.start();
    }

    private static KafkaMessageListenerContainer<String, OccurrenceEvent> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<String, OccurrenceEvent> cf =
                new DefaultKafkaConsumerFactory<String, OccurrenceEvent>(props
                        ,new StringDeserializer()
                        ,new JsonDeserializer<>(OccurrenceEvent.class,false));
        // ConcurrentKafkaListnerContainerFactory
        KafkaMessageListenerContainer<String, OccurrenceEvent> container =
                new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private static Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
}
