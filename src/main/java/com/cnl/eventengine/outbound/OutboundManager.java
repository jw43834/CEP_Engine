package com.cnl.eventengine.outbound;

import com.cnl.eventengine.config.KafkaProducerProperties;
import com.cnl.eventengine.domain.OccurrenceEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component(value = "OutboundManager")
@RequiredArgsConstructor
public class OutboundManager {
    private ArrayList<OutboundAdapter> outboundAdapterList;

    private final KafkaProducerProperties kafkaProducerProperties;

    private LinkedBlockingQueue<OccurrenceEvent> outboundQueue;

    private ExecutorService executorService;
    private OutboundAdapter kafkaOutboundAdapter;

    @PostConstruct
    public void initialize() {
        log.debug("Inbound Manager Initialize");

        outboundAdapterList = new ArrayList<OutboundAdapter>();

        outboundQueue = new LinkedBlockingQueue<OccurrenceEvent>();

        // TODO : DB에서 어댑터 설정정보 조회 -> 어댑터 초기화
        kafkaOutboundAdapter = new KafkaOutboundAdapter(kafkaProducerProperties);
        kafkaOutboundAdapter.initialize();

        // Create Thread Poll
        executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new PollingQueueRunner());
    }

    public void eventEnqueue(OccurrenceEvent occurrenceEvent) {
        // TODO : Enqueue Exception Handling
        try {
            log.debug("Enqueue OccurrenceEvent : {}",occurrenceEvent);
            outboundQueue.put(occurrenceEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Polling
    class PollingQueueRunner implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            log.debug("event Queue Polling Start");

            OccurrenceEvent occurrenceEvent;

            while(true){
                occurrenceEvent = outboundQueue.poll();
                if(occurrenceEvent!=null){
                    log.debug("Polling Occurrence Event : {}", occurrenceEvent);
                    kafkaOutboundAdapter.produce(occurrenceEvent);
                }

                Thread.sleep(100);
            }
        }
    }
}
