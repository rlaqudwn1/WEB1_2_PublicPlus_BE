package backend.dev.user.entity;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class User implements Persistable<String> {

    @Id
    private String userId;

    @Column(nullable = false,unique = true)
    @Email
    private String email;

    private String password;

    private String profile_image;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    private String description;

    @OneToMany(mappedBy = "user")
    private List<Oauth> oauthList = new ArrayList<>();
    //이후 테이블 연관관계에 따라 추가 예정입니다 ex) 태그,알림 등등
    private String fcmTokens;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public User(String userId, String email, String password, String profile, String nickname, String description) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.profile_image = profile;
        this.nickname = nickname;
        this.description = description;
        this.role = Role.USER;

    }


    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }

    public static UserDTO of(User user) {
        return new UserDTO(user.userId, user.email, user.profile_image, user.nickname, user.description,user.role);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeProfile(String profile) { this.profile_image = profile; }

    public void changeNickname(String nickname){ this.nickname = nickname; }

    public void changeDescription(String description){this.description = description; }

    public void addOauthList(Oauth oauth){
        this.oauthList.add(oauth);}

    public void deleteProfile() {
        if(profile_image ==null) return;

        File file = new File(profile_image);
        if (file.exists() && !file.delete()) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_DELETE_FAIL);
        }
    }
}
