package cz.vasic2000.icash.mvp.model.api;

import cz.vasic2000.icash.mvp.model.entity.Repository;
import cz.vasic2000.icash.mvp.model.entity.User;
import io.reactivex.Single;
import retrofit2.http.*;


import java.util.List;

public interface IDataSource {
    @GET("/users/{user}")
    Single<User> getUser(@Path("user") String username);

    @GET
    Single<List<Repository>> getUserRepos(@Url String url);
}
