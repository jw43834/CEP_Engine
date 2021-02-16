package com.cnl.eventengine.inbound;

import com.cnl.eventengine.config.KafkaConsumerProperties;
import com.cnl.eventengine.domain.CollectionEvent;
import com.cnl.eventengine.engine.EngineManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public final class KafkaInboundAdapter implements InboundAdapter {
    private final EngineManager engineManager;
    private final KafkaConsumerProperties kafkaConsumerProperties;

    @PostConstruct
    @Override
    public void initialize() {
        log.debug("Initialize Kafka Inbound Adapter");

        log.debug("Consumer Properties : {}" + kafkaConsumerProperties);

        ContainerProperties containerProps = new ContainerProperties(kafkaConsumerProperties.getTopic());
//        final CountDownLatch latch = new CountDownLatch(4);
        containerProps.setMessageListener(new MessageListener<String, CollectionEvent>() {

            @SneakyThrows
            @Override
            public void onMessage(ConsumerRecord<String, CollectionEvent> message) {
                log.info("received: " + message.value());
                //latch.countDown();
                // Message Send Queue
                // TODO: Exception Handling
                engineManager.eventEnqueue(message.value());
            }

        });

        KafkaMessageListenerContainer<String, CollectionEvent> container = createContainer(containerProps);
        container.setBeanName("kafkaConsumerContainer");
        container.start();
    }

    private KafkaMessageListenerContainer<String, CollectionEvent> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();

        JsonDeserializer<CollectionEvent> deserializer = new JsonDeserializer<>(CollectionEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        DefaultKafkaConsumerFactory<String, CollectionEvent> cf =
                new DefaultKafkaConsumerFactory<String, CollectionEvent>(props, new StringDeserializer(),
                        deserializer);
        // ConcurrentKafkaListnerContainerFactory
        KafkaMessageListenerContainer<String, CollectionEvent> container =
                new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;
    }

    @Override
    public void startConsume() {
        log.debug("Start Kafka Inbound Adapter Consuming");
    }


}
