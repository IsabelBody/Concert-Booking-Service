package proj.concert.service.mapper;

import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Booking;
import proj.concert.common.dto.BookingDTO;
import proj.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to convert between domain-model and DTO objects representing Bookings.
 */
public class BookingMapper {

    public static BookingDTO toDto(Booking booking) {
        List<SeatDTO> dtoSeats = new ArrayList<>();
        for(Seat seat: booking.getSeats()){
            SeatDTO dtoSeat = SeatMapper.toDto(seat);
            dtoSeats.add(dtoSeat);
        }

        BookingDTO dtoBooking = new BookingDTO(
                                    booking.getConcertId(),
                                    booking.getDate(),
                                    dtoSeats);
        return dtoBooking;
    }
}
