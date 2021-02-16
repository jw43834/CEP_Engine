package com.cnl.eventengine;

import com.cnl.eventengine.config.KafkaConsumerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationYamlReadTest {
    @Autowired
    private KafkaConsumerProperties kafkaInboundProperties;

    @Test
    void yamlFileTest() {
        String name = kafkaInboundProperties.getTopic();

        System.out.println("My name is " + name);
        assertThat(name).isEqualTo("test");
    }
}