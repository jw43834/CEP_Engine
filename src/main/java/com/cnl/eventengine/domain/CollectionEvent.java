package com.cnl.eventengine.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollectionEvent {
    private String deviceId;

    private String deviceModelId;

    //private int deviceSequence;
    //private int subDeviceSequence;

    private String occDate;

    private int value;
}
