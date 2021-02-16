package com.cnl.eventengine.inbound;

import com.cnl.eventengine.config.KafkaConsumerProperties;
import com.cnl.eventengine.engine.EngineManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Slf4j
@Component
@DependsOn(value = {"EngineManager", "OutboundManager"})
@RequiredArgsConstructor
public class InboundManager {
    private ArrayList<InboundAdapter> inboundAdapterList;
    private final KafkaConsumerProperties kafkaConsumerProperties;
    private final EngineManager engineManager;

    @PostConstruct
    public void initialize() {
        log.debug("Inbound Manager Initialize");

        log.debug("Consumer properties : {}",kafkaConsumerProperties);

        inboundAdapterList = new ArrayList<InboundAdapter>();

        // TODO : Get Adapter Configuration From DB
        InboundAdapter kafkaInboundAdapter = new KafkaInboundAdapter(engineManager, kafkaConsumerProperties);
        kafkaInboundAdapter.initialize();

        // TODO : 모든 모듈 로드 후 동작
        kafkaInboundAdapter.startConsume();
    }
}
