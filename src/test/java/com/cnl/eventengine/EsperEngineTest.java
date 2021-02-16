package com.cnl.eventengine;

import com.cnl.eventengine.domain.CollectionEvent;
import com.cnl.eventengine.processor.CollectionEventSubscriber;
import com.espertech.esper.client.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EsperEngineTest {
    private EPServiceProvider epService;
    private EPAdministrator epAdministrator;
    private EPRuntime epRuntime;

    public static void main(String[] args){
        EPServiceProvider epService = EPServiceProviderManager.getProvider("event-engine", addConfig());
        EPAdministrator epAdministrator = epService.getEPAdministrator();
        EPRuntime epRuntime = epService.getEPRuntime();

        epAdministrator.createEPL("select 'event1' as eventId, c.deviceId, c.occDate, c.value from CollectionEvent c where c.deviceId='a'")
                .setSubscriber(new TestCollectionEventSubscriber());

        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setDeviceId("a");
        collectionEvent.setOccDate("1234");
        collectionEvent.setValue(1);

        epRuntime.sendEvent(collectionEvent);
    }

    public static Configuration addConfig(){
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

    static class TestCollectionEventSubscriber {
        public void update(String eventId, String deviceId, String occDate, int value) {
            log.debug("Occur Event :{}",eventId);
        }
    }

}
