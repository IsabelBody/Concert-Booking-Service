package proj.concert.service.mapper;

import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

/**
 * Helper class to convert between domain-model and DTO objects representing Users.
 */
public class UserMapper {

    static User toDomainModel(proj.concert.common.dto.UserDTO dtoUser) {
        User fullUser = new User(
                dtoUser.getUsername(),
                dtoUser.getPassword());
        return fullUser;
    }

    static proj.concert.common.dto.UserDTO toDto(User user) {
        proj.concert.common.dto.UserDTO dtoUser =
                new proj.concert.common.dto.UserDTO(
                        user.getUsername(),
                        user.getPassword());
        return dtoUser;
    }
}
