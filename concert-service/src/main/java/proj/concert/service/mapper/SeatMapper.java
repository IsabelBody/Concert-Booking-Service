package proj.concert.service.mapper;

import proj.concert.service.domain.Seat;
import proj.concert.common.dto.SeatDTO;

/**
 * Helper class to convert between domain-model and DTO objects representing Seats.
 */
public class SeatMapper {

        public static SeatDTO toDto(Seat seat) {
            SeatDTO dtoSeat = new SeatDTO(
                                seat.getLabel(),
                                seat.getPrice());
            return dtoSeat;
        }
}
