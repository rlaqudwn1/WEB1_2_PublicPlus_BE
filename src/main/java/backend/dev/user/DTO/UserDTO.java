package backend.dev.user.DTO;

import backend.dev.user.entity.Role;

public record UserDTO(String userid,
                      String email,
                      String profilePath,
                      String nickname,
                      String description,
                      Role role
) {

}
