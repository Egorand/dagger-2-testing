package me.egorand.dagger_2_testing.di.modules;

import android.content.Context;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import me.egorand.dagger_2_testing.di.qualifiers.ActivityScope;
import me.egorand.dagger_2_testing.ui.activities.MainActivity;

@Module
public class MainActivityModule {

    private final MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope public Context provideActivityContext() {
        return activity;
    }

    @Provides public LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(activity);
    }
}
