package com.cnl.eventengine.outbound;

import com.cnl.eventengine.domain.OccurrenceEvent;

public interface OutboundAdapter {
    void initialize();
    void produce(OccurrenceEvent occurrenceEvent);
}
