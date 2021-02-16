package com.cnl.eventengine.processor;

import com.cnl.eventengine.domain.CollectionEvent;
import com.cnl.eventengine.outbound.OutboundManager;
import com.espertech.esper.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class EsperEventProcessor {
    private EPServiceProvider epService;
    private EPAdministrator epAdministrator;
    private EPRuntime epRuntime;
    private final OutboundManager outboundManager;

    @PostConstruct
    public void initialize() {
        // Get engine instance
        epService = EPServiceProviderManager.getProvider("event-engine", addConfig());
        epAdministrator = epService.getEPAdministrator();
        epRuntime = epService.getEPRuntime();

        // load EPL from DB
//        List<Event> eventList = eventService.selectEvent(null);
//        for(Event event: eventList){
//            epAdministrator.createEPL(event.getEplStatement(),event.getEventId());
//            log.debug("Deploy EPL Statement. id ={}, statement={}",event.getEventId(),event.getEplStatement());
//        }

        epAdministrator.createEPL("select 'event1' as eventId, c.deviceId,c.occDate, c.value from CollectionEvent c where c.deviceId='a'")
                .setSubscriber(new CollectionEventSubscriber(outboundManager));
    }

    public Configuration addConfig() {
        // Configure engine with event names to make the statements more readable.
        // This could also be done in a configuration file.
        Configuration configuration = new Configuration();

        // Add Event Type
        configuration.addEventType("CollectionEvent", CollectionEvent.class.getName());

        // Add Single Row Function
        //configuration.addPlugInSingleRowFunction("FunctionName","com.cnl.eventengine.className","MethodName");

        // Add Constant
        //configuration.addVariable("variablename","Strin.class","initializationValue");

        // Maximum Sub Expression
        configuration.setPatternMaxSubexpressions(0L);
        return configuration;
    }

    public void sendEvent(CollectionEvent collectionEvent) {
        epRuntime.sendEvent(collectionEvent);
    }
}
