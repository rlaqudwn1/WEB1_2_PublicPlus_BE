package backend.dev.user.service;

import backend.dev.user.domain.DTO.UserDTO;
import backend.dev.user.domain.DTO.UserJoinDTO;
import backend.dev.user.domain.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public UserDTO join(UserJoinDTO userJoinDTO) {
        String userid = UUID.randomUUID().toString();
        User user = User.builder()
                .userid(userid)
                .email(userJoinDTO.email())
                .password(userJoinDTO.password())
                .profile(userJoinDTO.profile())
                .build();
        userRepository.save(user);

        return User.of(user);
    }
}
