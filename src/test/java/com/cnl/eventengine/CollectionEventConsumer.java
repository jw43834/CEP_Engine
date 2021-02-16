package com.cnl.eventengine;

import com.cnl.eventengine.domain.CollectionEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.StringOrBytesSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class CollectionEventConsumer {
    public static void main(String[] args){
        ContainerProperties containerProps = new ContainerProperties("test");
        final CountDownLatch latch = new CountDownLatch(4);
        containerProps.setMessageListener(new MessageListener<String, CollectionEvent>() {

            @Override
            public void onMessage(ConsumerRecord<String, CollectionEvent> message) {
                System.out.println("received: " + message.value());
                latch.countDown();
            }

        });

        KafkaMessageListenerContainer<String, CollectionEvent> container = createContainer(containerProps);
        container.start();
    }

    private static KafkaMessageListenerContainer<String, CollectionEvent> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<String, CollectionEvent> cf =
                new DefaultKafkaConsumerFactory<String, CollectionEvent>(props
                        ,new StringDeserializer()
                        ,new JsonDeserializer<>(CollectionEvent.class,false));
        // ConcurrentKafkaListnerContainerFactory
        KafkaMessageListenerContainer<String, CollectionEvent> container =
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
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
}
