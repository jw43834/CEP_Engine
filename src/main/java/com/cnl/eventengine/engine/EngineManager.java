package com.cnl.eventengine.engine;

import com.cnl.eventengine.domain.CollectionEvent;
import com.cnl.eventengine.outbound.OutboundManager;
import com.cnl.eventengine.processor.EsperEventProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component(value = "EngineManager")
@Getter
@RequiredArgsConstructor
public class EngineManager {
    private final EsperEventProcessor esperEventProcessor;

//    private final InboundManager inboundManager;

//    private final OutboundManager outboundManager;

    private LinkedBlockingQueue<CollectionEvent> eventQueue;

    private ExecutorService executorService;

    @PostConstruct
    public void initialize() {
        log.debug("Engine Manager Initialize");

        eventQueue = new LinkedBlockingQueue<CollectionEvent>();

        // Create Thread Poll
        executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new PollingRunner());
    }

    public void eventEnqueue(CollectionEvent collectionEvent) throws InterruptedException {
        // TODO : Enqueue Exception Handling
        eventQueue.put(collectionEvent);
    }

    class PollingRunner implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            log.debug("event Queue Polling Start");

            CollectionEvent collectionEvent;

            while(true){
                collectionEvent = eventQueue.poll();
                if(collectionEvent!=null){
                    log.debug("Polling Event : {}", collectionEvent);
                    esperEventProcessor.sendEvent(collectionEvent);
                }

                Thread.sleep(100);
            }
        }
    }
}
