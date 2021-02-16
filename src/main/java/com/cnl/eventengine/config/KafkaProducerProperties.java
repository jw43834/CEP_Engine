package com.cnl.eventengine.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Getter
@ToString
@Configuration
@ConfigurationProperties(prefix="adapter.spring.kafka.producer")
public class KafkaProducerProperties {
    private String bootstrapServers;
    private String topic;
}
