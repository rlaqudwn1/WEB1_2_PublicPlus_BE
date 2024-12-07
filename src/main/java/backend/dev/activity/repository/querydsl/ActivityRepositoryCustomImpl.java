package backend.dev.activity.repository.querydsl;

import backend.dev.activity.entity.Activity;
import backend.dev.activity.entity.QActivity;
import backend.dev.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Component
@Repository
@RequiredArgsConstructor
public class ActivityRepositoryCustomImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    public User findUserByAdmin(Activity activity) {
        QActivity qActivity = QActivity.activity;
        queryFactory.selectFrom(qActivity)
                .from(qActivity.participants.any())
                .where();
        return null;
    }
}
