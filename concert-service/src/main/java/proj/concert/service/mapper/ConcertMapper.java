package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;

/**
 * Helper class to convert between domain-model and DTO objects representing Concerts.
 */
public class ConcertMapper {

    static Concert toDomainModel(proj.concert.common.dto.ConcertDTO dtoConcert) {
        Concert fullConcert = new Concert(
                dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb());
        return fullConcert;
    }

    static proj.concert.common.dto.ConcertDTO toDto(Concert concert) {
        proj.concert.common.dto.ConcertDTO dtoConcert =
                new proj.concert.common.dto.ConcertDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName(),
                        concert.getBlurb());
        return dtoConcert;
    }
}