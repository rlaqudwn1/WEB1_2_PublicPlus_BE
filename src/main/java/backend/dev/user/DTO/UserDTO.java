package backend.dev.user.DTO;

import backend.dev.user.entity.Role;

public record UserDTO(String userid,
                      String email,
                      String profile_image,
                      String nickname,
                      String description,
                      Role role
) {

}
