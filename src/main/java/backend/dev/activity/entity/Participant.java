package backend.dev.activity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 참석자 이메일
    private String name; // 참석자 이름
    private String status; // 참석 상태 (예: 참석, 미참석, 보류)

    @ManyToOne
    private Activity activity; // 연결된 활동
}
