package me.egorand.dagger_2_testing;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import me.egorand.dagger_2_testing.data.Datastore;
import me.egorand.dagger_2_testing.data.Repo;
import me.egorand.dagger_2_testing.data.ReposDatabaseHelper;
import me.egorand.dagger_2_testing.data.ReposDiskDatastore;
import me.egorand.dagger_2_testing.data.ReposMemoryDatastore;
import me.egorand.dagger_2_testing.di.components.AppComponent;
import me.egorand.dagger_2_testing.di.qualifiers.AppScope;
import me.egorand.dagger_2_testing.di.qualifiers.Disk;
import me.egorand.dagger_2_testing.di.qualifiers.Memory;
import me.egorand.dagger_2_testing.rest.GithubApiClient;
import me.egorand.dagger_2_testing.ui.activities.MainActivity;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class BaseDataLoadingTest {

    @Module
    public static class TestAppModule {

        private final App app;

        public TestAppModule(App app) {
            this.app = app;
        }

        @Provides @AppScope public Context provideAppContext() {
            return app;
        }

        @Provides @Singleton @Memory public Datastore<Repo> provideMemoryDatastore() {
            return new ReposMemoryDatastore();
        }

        @Provides @Singleton @Disk public Datastore<Repo> provideDiskDatastore() {
            return new ReposDiskDatastore(new ReposDatabaseHelper(app));
        }

        @Provides public GithubApiClient provideGithubApiClient() {
            GithubApiClient mockGithubApiClient = mock(GithubApiClient.class);
            when(mockGithubApiClient.getRepos(anyString())).thenReturn(Observable.empty());
            return mockGithubApiClient;
        }
    }

    @Component(modules = TestAppModule.class)
    @Singleton
    public interface TestAppComponent extends AppComponent {

        void inject(MemoryDataLoadingTest test);

        void inject(DiskDataLoadingTest test);

        void inject(NetworkDataLoadingTest test);
    }

    @Rule public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        App app = (App) instrumentation.getTargetContext().getApplicationContext();
        TestAppComponent appComponent = DaggerBaseDataLoadingTest_TestAppComponent.builder()
                .testAppModule(new TestAppModule(app))
                .build();
        app.setAppComponent(appComponent);
        injectChild(appComponent);
    }

    protected abstract void injectChild(TestAppComponent appComponent);

    @Test
    public void dataLoadedProperly() {
        rule.launchActivity(new Intent());

        for (int position = 0; position < TestData.REPOS.size(); position++) {
            Repo repo = TestData.REPOS.get(position);
            onView(withId(android.R.id.list)).perform(scrollToPosition(position));
            onView(withText(repo.name)).check(matches(withText(repo.name)));
            onView(withText(repo.description)).check(matches(withText(repo.description)));
        }
    }
}
