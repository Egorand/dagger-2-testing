package me.egorand.dagger_2_testing;

import android.app.Application;

import me.egorand.dagger_2_testing.di.components.AppComponent;
import me.egorand.dagger_2_testing.di.components.DaggerAppComponent;
import me.egorand.dagger_2_testing.di.modules.AppModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
    }

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public AppComponent appComponent() {
        return appComponent;
    }
}
