package backend.dev.user.entity;

import backend.dev.activity.entity.Activity;
import backend.dev.notification.entity.FCMToken;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.UserDTO;
import jakarta.persistence.*;
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
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false,unique = true)
    @Email
    private String email;

    private String password;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    private String description;

    @OneToMany(mappedBy = "user")
    private List<Oauth> oauthList = new ArrayList<>();
    //이후 테이블 연관관계에 따라 추가 예정입니다 ex) 태그,알림 등등
    private String fcmToken;

    private String googleCalenderId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Activity> events = new ArrayList<>();



    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public User(String userId, String email, String password, String profile, String nickname, String description) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.profilePath = profile;
        this.nickname = nickname;
        this.description = description;
        this.fcmToken = fcmToken;
        this.googleCalenderId = googleCalenderId;
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
        return new UserDTO(user.userId, user.email, user.profilePath, user.nickname, user.description,user.role);
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeProfile(String profile) { this.profilePath = profile; }

    public void changeNickname(String nickname){ this.nickname = nickname; }

    public void changeDescription(String description){this.description = description; }

    public void addOauthList(Oauth oauth){
        this.oauthList.add(oauth);}

    public void deleteProfile() {
        if(profilePath==null) return;

        File file = new File(profilePath);
        if (file.exists() && !file.delete()) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_DELETE_FAIL);
        }
    }
}
