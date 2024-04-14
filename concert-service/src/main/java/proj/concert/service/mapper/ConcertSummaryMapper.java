package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;

/**
 * Helper class to convert between Concert domain-model and ConcertSummary DTO object.
 */
public class ConcertSummaryMapper {

    static Concert toDomainModel(proj.concert.common.dto.ConcertSummaryDTO dtoConcertSummary) {
        Concert fullConcertSummary = new Concert(
                dtoConcertSummary.getId(),
                dtoConcertSummary.getTitle(),
                dtoConcertSummary.getImageName());
        return fullConcertSummary;
    }

    static proj.concert.common.dto.ConcertSummaryDTO toDto(Concert concert) {
        proj.concert.common.dto.ConcertSummaryDTO dtoConcertSummary =
                new proj.concert.common.dto.ConcertSummaryDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName());
        return dtoConcertSummary;
    }
}