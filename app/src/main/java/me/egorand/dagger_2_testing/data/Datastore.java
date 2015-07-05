package me.egorand.dagger_2_testing.data;

import java.util.List;

import rx.Observable;

public interface Datastore<T> {

    void save(List<T> data);

    Observable<List<T>> queryAll();

    void clear();
}
