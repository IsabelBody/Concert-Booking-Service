package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BookingKey implements Serializable {
    private User user;
    private LocalDateTime date;
    private long concertId;
}