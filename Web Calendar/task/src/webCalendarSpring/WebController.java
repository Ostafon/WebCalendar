package webCalendarSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/event")
public class WebController {

    @Autowired
    private EventRepository repository;

    @PostMapping()
    public ResponseEntity<Map<String, Object>> addEvent(@RequestBody Event event) {
        if (event == null || event.getEvent() == null || event.getEvent().trim().isEmpty() || event.getDate() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Event savedEvent = repository.save(event);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "The event has been added!");
        response.put("event", savedEvent.getEvent());
        response.put("date", savedEvent.getDate().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Event>> getEventsByTimeRange(
            @RequestParam(required = false) String start_time,
            @RequestParam(required = false) String end_time) {

        List<Event> events;

        if (start_time != null && end_time != null) {
            LocalDate startDate = LocalDate.parse(start_time);
            LocalDate endDate = LocalDate.parse(end_time);
            events = repository.findByDateBetween(startDate, endDate);
        } else {
            events = repository.findAll();
        }

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(events);
        }
    }



    @GetMapping("/today")
    public ResponseEntity<List<Event>> getTodayEvent() {
        LocalDate today = LocalDate.now();
        List<Event> todayEvents = repository.findByDate(today);
        return new ResponseEntity<>(todayEvents, HttpStatus.OK);
    }

    // GET endpoint to fetch event by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Integer id) {
        Optional<Event> optionalEvent = repository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("The event doesn't exist!"));
        }
        Event event = optionalEvent.get();
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEventById(@PathVariable Integer id) {
        Optional<Event> optionalEvent = repository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("The event doesn't exist!"));
        }

        Event eventToDelete = optionalEvent.get();
        repository.delete(eventToDelete);

        // Prepare response body with deleted event info
        Map<String, Object> response = new HashMap<>();
        response.put("id", eventToDelete.getId());
        response.put("event", eventToDelete.getEvent());
        response.put("date", eventToDelete.getDate().toString());

        return ResponseEntity.ok(response);
    }



    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
