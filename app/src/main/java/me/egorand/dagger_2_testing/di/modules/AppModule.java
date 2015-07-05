package me.egorand.dagger_2_testing.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.egorand.dagger_2_testing.App;
import me.egorand.dagger_2_testing.data.Datastore;
import me.egorand.dagger_2_testing.data.Repo;
import me.egorand.dagger_2_testing.data.ReposDatabaseHelper;
import me.egorand.dagger_2_testing.data.ReposDiskDatastore;
import me.egorand.dagger_2_testing.data.ReposMemoryDatastore;
import me.egorand.dagger_2_testing.di.qualifiers.Disk;
import me.egorand.dagger_2_testing.di.qualifiers.Memory;
import me.egorand.dagger_2_testing.rest.GithubApiClient;
import retrofit.RestAdapter;

@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton public Context provideAppContext() {
        return app;
    }

    @Provides public GithubApiClient provideGithubApiClient() {
        return new RestAdapter.Builder()
                .setEndpoint(GithubApiClient.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(GithubApiClient.class);
    }

    @Provides @Singleton @Memory public Datastore<Repo> provideMemoryDatastore() {
        return new ReposMemoryDatastore();
    }

    @Provides @Singleton @Disk public Datastore<Repo> provideDaskDatastore() {
        return new ReposDiskDatastore(new ReposDatabaseHelper(app));
    }
}
