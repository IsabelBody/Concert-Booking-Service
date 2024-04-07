package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SeatKey implements Serializable {
    private String label;
    private LocalDateTime date;
}