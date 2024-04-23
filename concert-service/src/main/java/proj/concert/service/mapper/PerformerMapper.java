package proj.concert.service.mapper;

import proj.concert.service.domain.Performer;
import proj.concert.common.dto.PerformerDTO;

public class PerformerMapper {

        public static PerformerDTO toDto(Performer performer) {
            PerformerDTO dtoPerformer = new PerformerDTO(
                                            performer.getId(),
                                            performer.getName(),
                                            performer.getImageName(),
                                            performer.getGenre(),
                                            performer.getBlurb());
            return dtoPerformer;
        }
}
