package me.egorand.dagger_2_testing.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import me.egorand.dagger_2_testing.rest.GithubApiClient;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReposLoaderTest {

    private ReposLoader reposLoader;

    private GithubApiClient mockGithubApiClient;
    private Datastore<Repo> mockDiskDatastore;
    private Datastore<Repo> mockMemoryDatastore;

    private TestSubscriber<List<Repo>> testSubscriber;

    @Before
    public void setUp() {
        mockGithubApiClient = mock(GithubApiClient.class);
        mockDiskDatastore = mock(Datastore.class);
        mockMemoryDatastore = mock(Datastore.class);
        reposLoader = new ReposLoader(mockGithubApiClient, mockDiskDatastore, mockMemoryDatastore);

        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void dataLoadedFromMemoryIfExists() {
        when(mockMemoryDatastore.queryAll()).thenReturn(Observable.just(TestData.REPOS));
        when(mockDiskDatastore.queryAll()).thenReturn(Observable.<List<Repo>>empty());
        when(mockGithubApiClient.getRepos(TestData.USERNAME)).thenReturn(Observable.<List<Repo>>empty());

        reposLoader.loadRepos(TestData.USERNAME).subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
    }

    @Test
    public void dataLoadedFromDiskIfExists() {
        when(mockMemoryDatastore.queryAll()).thenReturn(Observable.<List<Repo>>empty());
        when(mockDiskDatastore.queryAll()).thenReturn(Observable.just(TestData.REPOS));
        when(mockGithubApiClient.getRepos(TestData.USERNAME)).thenReturn(Observable.<List<Repo>>empty());

        reposLoader.loadRepos(TestData.USERNAME).subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
        verify(mockMemoryDatastore).clear();
        verify(mockMemoryDatastore).save(TestData.REPOS);
    }

    @Test
    public void dataLoadedFromNetworkIfNoCacheExists() {
        when(mockMemoryDatastore.queryAll()).thenReturn(Observable.<List<Repo>>empty());
        when(mockDiskDatastore.queryAll()).thenReturn(Observable.<List<Repo>>empty());
        when(mockGithubApiClient.getRepos(TestData.USERNAME)).thenReturn(Observable.just(TestData.REPOS));

        reposLoader.loadRepos(TestData.USERNAME).subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
        verify(mockMemoryDatastore).clear();
        verify(mockMemoryDatastore).save(TestData.REPOS);
        verify(mockDiskDatastore).clear();
        verify(mockDiskDatastore).save(TestData.REPOS);
    }
}