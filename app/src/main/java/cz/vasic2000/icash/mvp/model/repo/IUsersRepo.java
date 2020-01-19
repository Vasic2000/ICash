package cz.vasic2000.icash.mvp.model.repo;

import java.util.List;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.reactivex.Single;

public interface IUsersRepo {
    Single<User> getUser(String username);
    Single<List<Repository>> getUserRepos(User user);
}
