package backend.dev.user.entity;

import backend.dev.user.DTO.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import java.awt.Image;
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
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    private String description;

    //이후 테이블 연관관계에 따라 추가 예정입니다 ex) 태그,알림 등등

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public User(String userid, String email, String password, String profile, String nickname, String description) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.profilePath = profile;
        this.nickname = nickname;
        this.description = description;
        this.role = Role.USER;
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
        return new UserDTO(user.userid, user.email, user.profilePath, user.nickname, user.description);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeProfile(String profile) { this.profilePath = profile; }

    public void changeNickname(String nickname){ this.nickname = nickname; }

    public void changeDescription(String description){this.description = description; }

    public void deleteProfile() {

    }
}
