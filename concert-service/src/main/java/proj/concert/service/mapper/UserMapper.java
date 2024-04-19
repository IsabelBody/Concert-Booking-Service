package proj.concert.service.mapper;

import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

/**
 * Helper class to convert between domain-model and DTO objects representing Users.
 */
public class UserMapper {

    public static User toDomainModel(UserDTO dtoUser) {
        User fullUser = new User(
                            dtoUser.getUsername(),
                            dtoUser.getPassword());
        return fullUser;
    }

    public static UserDTO toDto(User user) {
        UserDTO dtoUser = new UserDTO(
                            user.getUsername(),
                            user.getPassword());
        return dtoUser;
    }
}
