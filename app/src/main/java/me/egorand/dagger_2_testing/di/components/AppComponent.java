package me.egorand.dagger_2_testing.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.egorand.dagger_2_testing.data.Datastore;
import me.egorand.dagger_2_testing.data.Repo;
import me.egorand.dagger_2_testing.data.ReposLoader;
import me.egorand.dagger_2_testing.di.modules.AppModule;
import me.egorand.dagger_2_testing.di.modules.MainActivityModule;
import me.egorand.dagger_2_testing.di.qualifiers.Disk;
import me.egorand.dagger_2_testing.di.qualifiers.Memory;
import me.egorand.dagger_2_testing.rest.GithubApiClient;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    MainActivityComponent plus(MainActivityModule module);

    @Singleton Context appContext();

    GithubApiClient githubApiClient();

    @Memory Datastore<Repo> memoryDatastore();

    @Disk Datastore<Repo> diskDatastore();

    ReposLoader reposLoader();
}
