package me.egorand.dagger_2_testing.data;

import java.util.List;

import javax.inject.Inject;

import me.egorand.dagger_2_testing.di.qualifiers.Disk;
import me.egorand.dagger_2_testing.di.qualifiers.Memory;
import me.egorand.dagger_2_testing.rest.GithubApiClient;
import rx.Observable;

public class ReposLoader {

    private final GithubApiClient githubApiClient;
    private final Datastore<Repo> diskDatastore;
    private final Datastore<Repo> memoryDatastore;

    @Inject
    public ReposLoader(GithubApiClient githubApiClient,
                       @Disk Datastore<Repo> diskDatastore,
                       @Memory Datastore<Repo> memoryDatastore) {
        this.githubApiClient = githubApiClient;
        this.diskDatastore = diskDatastore;
        this.memoryDatastore = memoryDatastore;
    }

    public Observable<List<Repo>> loadRepos(String username) {
        Observable<List<Repo>> memory = memoryDatastore.queryAll();
        Observable<List<Repo>> disk = diskDatastore.queryAll().doOnNext(repos -> {
            memoryDatastore.clear();
            memoryDatastore.save(repos);
        });
        Observable<List<Repo>> network = githubApiClient.getRepos(username).doOnNext(repos -> {
            diskDatastore.clear();
            diskDatastore.save(repos);
            memoryDatastore.clear();
            memoryDatastore.save(repos);
        });
        return Observable.concat(memory, disk, network).first();
    }
}
