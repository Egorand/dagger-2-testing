package me.egorand.dagger_2_testing.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.egorand.dagger_2_testing.data.Datastore;
import me.egorand.dagger_2_testing.data.Repo;
import me.egorand.dagger_2_testing.data.ReposLoader;
import me.egorand.dagger_2_testing.di.modules.AppModule;
import me.egorand.dagger_2_testing.di.qualifiers.AppScope;
import me.egorand.dagger_2_testing.di.qualifiers.Disk;
import me.egorand.dagger_2_testing.di.qualifiers.Memory;
import me.egorand.dagger_2_testing.rest.GithubApiClient;
import me.egorand.dagger_2_testing.ui.activities.MainActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    @AppScope Context appContext();

    GithubApiClient githubApiClient();

    @Memory Datastore<Repo> memoryDatastore();

    @Disk Datastore<Repo> diskDatastore();

    ReposLoader reposLoader();
}
