package webCalendarSpring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByDate(LocalDate today);
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
