package me.egorand.dagger_2_testing.di.components;

import android.content.Context;
import android.view.LayoutInflater;

import dagger.Subcomponent;
import me.egorand.dagger_2_testing.di.modules.MainActivityModule;
import me.egorand.dagger_2_testing.di.qualifiers.ActivityScope;
import me.egorand.dagger_2_testing.ui.activities.MainActivity;

@ActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity activity);

    @ActivityScope Context activityContext();

    LayoutInflater layoutInflater();
}
