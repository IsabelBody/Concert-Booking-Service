package proj.concert.service.mapper;

import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Concert;
import proj.concert.common.dto.ConcertDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to convert between domain-model and DTO objects representing Concerts.
 */
public class ConcertMapper {

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert = new ConcertDTO(
                                    concert.getId(),
                                    concert.getTitle(),
                                    concert.getImageName(),
                                    concert.getBlurb());

        // map concert's dates to dto's dates list
        dtoConcert.getDates().addAll(concert.getDates());

        // map concert's performers to dto's performers list
        List<PerformerDTO> performers = new ArrayList<>();
        concert.getPerformers().forEach(performer -> performers.add(PerformerMapper.toDto(performer)));
        dtoConcert.setPerformers(performers);

        return dtoConcert;
    }
}