package com.cnl.eventengine.api.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @GetMapping(value = "/{eventId}")
    public String getEvent(String eventId) {
        return "Get Event";
    }

    @GetMapping(value = "/")
    public String getEvents(String eventId) {
        return "Get Events";
    }

    @PostMapping(value = "/")
    public String postEvent() {
        return "Post Event";
    }

    @DeleteMapping(value = "/{eventId}")
    public String deleteEvent() {
        return "Delete Event";
    }
}
