package com.cnl.eventengine.api.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface EventService {
    Page<Event> findAll(Pageable pageable);
    Optional<Event> findById(String eventId);
    void save(Event event);
    void delete(String eventId);
}
