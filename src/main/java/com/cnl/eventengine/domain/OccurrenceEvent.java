package com.cnl.eventengine.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OccurrenceEvent {
    private String eventId;
    private String occDate;
}
