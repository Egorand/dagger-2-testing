package me.egorand.dagger_2_testing.rest;

import java.util.List;

import me.egorand.dagger_2_testing.data.Repo;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GithubApiClient {

    Endpoint ENDPOINT = Endpoints.newFixedEndpoint("https://api.github.com");

    @GET("/users/{username}/repos")
    Observable<List<Repo>> getRepos(@Path("username") String username);
}
