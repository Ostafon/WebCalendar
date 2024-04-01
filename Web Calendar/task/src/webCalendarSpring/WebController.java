package webCalendarSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/event")
public class WebController {

    @Autowired
    private EventRepository repository;

    @PostMapping()
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        Event savedEvent = repository.save(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = repository.findAll();
        return new ResponseEntity<>(events, events.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<List<Event>> getTodayEvent() {
        LocalDate today = LocalDate.now();
        List<Event> todayEvents = repository.findByDate(today);
        return new ResponseEntity<>(todayEvents, todayEvents.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

}
