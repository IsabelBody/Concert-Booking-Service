package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;
import proj.concert.common.dto.ConcertSummaryDTO;

/**
 * Helper class to convert between Concert domain-model and ConcertSummary DTO object.
 */
public class ConcertSummaryMapper {

    public static ConcertSummaryDTO toDto(Concert concert) {
        ConcertSummaryDTO dtoConcertSummary = new ConcertSummaryDTO(
                                                concert.getId(),
                                                concert.getTitle(),
                                                concert.getImageName());
        return dtoConcertSummary;
    }
}