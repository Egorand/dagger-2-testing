package me.egorand.dagger_2_testing.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import rx.observers.TestSubscriber;

public class ReposMemoryDatastoreTest {

    private ReposMemoryDatastore datastore;

    private TestSubscriber<List<Repo>> testSubscriber;

    @Before
    public void setUp() {
        datastore = new ReposMemoryDatastore();
        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void queryingEmptyDatastoreYieldsEmptyObservable() {
        datastore.queryAll().subscribe(testSubscriber);
        testSubscriber.assertNoValues();
    }

    @Test
    public void queryingClearedDatastoreYieldsEmptyObservable() {
        datastore.save(TestData.REPOS);
        datastore.clear();
        datastore.queryAll().subscribe(testSubscriber);
        testSubscriber.assertNoValues();
    }

    @Test
    public void queryingFullDatastoreYieldsProperData() {
        datastore.save(TestData.REPOS);
        datastore.queryAll().subscribe(testSubscriber);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
    }

    @Test
    public void datastoreClearedBeforeSecondSaving() {
        datastore.save(TestData.REPOS);
        datastore.save(TestData.REPOS);
        datastore.queryAll().subscribe(testSubscriber);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
    }
}