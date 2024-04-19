package proj.concert.service.mapper;

import proj.concert.service.domain.Booking;
import proj.concert.common.dto.BookingDTO;

/**
 * Helper class to convert between domain-model and DTO objects representing Bookings.
 */
public class BookingMapper {

    public static Booking toDomainModel(BookingDTO dtoBooking) {
        Booking fullBooking = new Booking(
                                    dtoBooking.getConcertId(),
                                    dtoBooking.getDate(),
                                    dtoBooking.getSeats());
        return fullBooking;
    }

    public static BookingDTO toDto(Booking booking) {
        BookingDTO dtoBooking = new BookingDTO(
                                    booking.getConcertId(),
                                    booking.getDate(),
                                    booking.getSeats());
        return dtoBooking;
    }
}
