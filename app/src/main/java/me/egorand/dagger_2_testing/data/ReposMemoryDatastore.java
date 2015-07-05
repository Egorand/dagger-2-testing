package me.egorand.dagger_2_testing.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class ReposMemoryDatastore implements Datastore<Repo> {

    private List<Repo> repos;

    @Override
    public void save(List<Repo> data) {
        clear();
        repos = new ArrayList<>(data);
    }

    @Override
    public Observable<List<Repo>> queryAll() {
        if (repos == null) {
            return Observable.empty();
        }
        return Observable.just(Collections.unmodifiableList(repos));
    }

    @Override
    public void clear() {
        if (repos != null) {
            repos.clear();
            repos = null;
        }
    }
}
