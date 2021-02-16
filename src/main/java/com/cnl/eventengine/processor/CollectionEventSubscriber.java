package com.cnl.eventengine.processor;

import com.cnl.eventengine.domain.OccurrenceEvent;
import com.cnl.eventengine.outbound.OutboundManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class CollectionEventSubscriber {
    private final OutboundManager outboundManager;

    public void update(String eventId, String deviceId, String occDate, int value) {
        log.debug("Occur Event :{}", eventId);
        outboundManager.eventEnqueue(new OccurrenceEvent(eventId, occDate));
    }
}
