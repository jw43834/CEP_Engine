package com.cnl.eventengine.api.event;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_sequence")
    private Long eventSequence;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "epl_statement")
    private String eplStatement;
}
