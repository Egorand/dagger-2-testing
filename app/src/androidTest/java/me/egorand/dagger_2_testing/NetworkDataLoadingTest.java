package me.egorand.dagger_2_testing;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import me.egorand.dagger_2_testing.rest.GithubApiClient;
import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class NetworkDataLoadingTest extends BaseDataLoadingTest {

    @Inject GithubApiClient githubApiClient;

    @Override
    protected void injectChild(TestAppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void setUp() {
        super.setUp();
        when(githubApiClient.getRepos(anyString()))
                .thenReturn(Observable.just(TestData.REPOS).delay(300, TimeUnit.MILLISECONDS));
    }
}
