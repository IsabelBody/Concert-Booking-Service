package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;
import proj.concert.common.dto.ConcertDTO;

/**
 * Helper class to convert between domain-model and DTO objects representing Concerts.
 */
public class ConcertMapper {

    static Concert toDomainModel(ConcertDTO dtoConcert) {
        Concert fullConcert = new Concert(
                                    dtoConcert.getId(),
                                    dtoConcert.getTitle(),
                                    dtoConcert.getImageName(),
                                    dtoConcert.getBlurb());
        return fullConcert;
    }

    static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert = new ConcertDTO(
                                    concert.getId(),
                                    concert.getTitle(),
                                    concert.getImageName(),
                                    concert.getBlurb());
        return dtoConcert;
    }
}