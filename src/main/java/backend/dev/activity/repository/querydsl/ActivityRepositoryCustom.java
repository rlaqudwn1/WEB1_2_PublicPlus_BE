package backend.dev.activity.repository.querydsl;

import backend.dev.activity.entity.Activity;
import backend.dev.user.entity.User;

public interface ActivityRepositoryCustom {
    User findUserByAdmin(Activity activity);
}
