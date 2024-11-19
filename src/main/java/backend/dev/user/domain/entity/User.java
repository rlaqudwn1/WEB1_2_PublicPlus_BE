package backend.dev.user.domain.entity;

import backend.dev.user.domain.DTO.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
public class User implements Persistable<String> {
    @Id
    private String userid;

    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    private String profile;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public User(String userid, String email, String password, String profile) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }


    @Override
    public String getId() {
        return userid;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }

    public static UserDTO of(User user) {
        return new UserDTO(user.userid, user.email, user.profile);
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .userid(userDTO.userid())
                .email(userDTO.email())
                .profile(userDTO.profile())
                .build();
    }
}
