package proj.concert.service.mapper;

import proj.concert.service.domain.Seat;

/**
 * Helper class to convert between domain-model and DTO objects representing Seats.
 */
public class SeatMapper {
        static Seat toDomainModel(proj.concert.common.dto.SeatDTO dtoSeat) {
            Seat fullSeat = new Seat(
                    dtoSeat.getLabel(),
                    dtoSeat.getPrice());
            return fullSeat;
        }

        static proj.concert.common.dto.SeatDTO toDto(Seat seat) {
            proj.concert.common.dto.SeatDTO dtoSeat =
                    new proj.concert.common.dto.SeatDTO(
                            seat.getLabel(),
                            seat.getPrice());
            return dtoSeat;
        }
}
