package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;

/**
 * Helper class to convert between domain-model and DTO objects representing Bookings.
 */
public class BookingMapper {

    static Booking toDomainModel(proj.concert.common.dto.BookingDTO dtoBooking) {
        Booking fullBooking = new Booking(
                dtoBooking.getConcertId(),
                dtoBooking.getDate(),
                dtoBooking.getSeats());
        return fullBooking;
    }

    static proj.concert.common.dto.BookingDTO toDto(Booking booking) {
        proj.concert.common.dto.BookingDTO dtoBooking =
                new proj.concert.common.dto.BookingDTO(
                        booking.getConcertId(),
                        booking.getDate(),
                        booking.getSeats());
        return dtoBooking;
    }
}
