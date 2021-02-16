package com.cnl.eventengine.api.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Optional<Event> findById(String eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public void delete(String eventId) {
        eventRepository.deleteById(eventId);
    }
}
