package proj.concert.service.mapper;

public class PerformerMapper {

        static Performer toDomainModel(proj.concert.common.dto.PerformerDTO dtoPerformer) {
            Performer fullPerformer = new Performer(
                    dtoPerformer.getId(),
                    dtoPerformer.getName(),
                    dtoPerformer.getImageName(),
                    dtoPerformer.getGenre(),
                    dtoPerformer.getBlurb());
            return fullPerformer;
        }

        static proj.concert.common.dto.PerformerDTO toDto(Performer performer) {
            proj.concert.common.dto.PerformerDTO dtoPerformer =
                    new proj.concert.common.dto.PerformerDTO(
                            performer.getId(),
                            performer.getName(),
                            performer.getImageName(),
                            performer.getGenre(),
                            performer.getBlurb());
            return dtoPerformer;
        }
}
