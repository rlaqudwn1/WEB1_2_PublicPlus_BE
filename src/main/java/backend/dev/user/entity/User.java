package backend.dev.user.entity;

import backend.dev.activity.entity.Activity;
import backend.dev.activity.entity.ActivityParticipants;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.notification.entity.Notification;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users") // H2에서 예약어라 테이블명 변경
public class User implements Persistable<String> {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    private String password;

    private String profilePath;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nickname;

    private String description;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Oauth> oauthList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    //이후 테이블 연관관계에 따라 추가 예정입니다 ex) 태그,알림 등등
    private String fcmToken;

    private String googleCalenderId;

    //스키마 정의를 위해 임의로 생성했습니다 추후 수정 부탁드리겠습니다
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ActivityParticipants> activityParticipants = new HashSet<>();

    // Participant와의 관계 추가 성운 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @Builder
    public User(String userId, String email, String password, String profile, String nickname, String description,
                Role role) {

        this.userId = userId;
        this.email = email;
        this.password = password;
        this.profilePath = profile;
        this.nickname = nickname;
        this.description = description;
        this.fcmToken = fcmToken;
        this.googleCalenderId = googleCalenderId;
        this.role = role != null ? role : Role.USER; // null일 경우 기본값 설정
    }


    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfile(String profile) {
        this.profilePath = profile;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void addOauthList(Oauth oauth) {
        oauth.addUser(this);
        this.oauthList.add(oauth);
    }

    public void deleteProfile() {
        if (profilePath == null) {
            return;
        }
        File file = new File(profilePath);
        if (file.exists() && !file.delete()) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_DELETE_FAIL);
        }
    }

    // 테스트를 위해 ID를 메서드 설정했습니다.
    public void setId(String user123) {

    }

    public String getUserId() {
        return userId;
    }
}
