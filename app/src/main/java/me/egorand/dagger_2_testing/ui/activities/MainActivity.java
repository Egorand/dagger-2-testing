package me.egorand.dagger_2_testing.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import me.egorand.dagger_2_testing.App;
import me.egorand.dagger_2_testing.R;
import me.egorand.dagger_2_testing.data.ReposLoader;
import me.egorand.dagger_2_testing.di.components.MainActivityComponent;
import me.egorand.dagger_2_testing.di.modules.MainActivityModule;
import me.egorand.dagger_2_testing.ui.adapters.ReposAdapter;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MainActivityComponent mainActivityComponent;

    private RecyclerView reposRecyclerView;
    private View progress;

    @Inject ReposLoader reposLoader;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainActivityComponent = ((App) getApplication()).appComponent().plus(new MainActivityModule(this));
        mainActivityComponent.inject(this);

        reposRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        reposRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progress = findViewById(android.R.id.progress);

        loadRepos();
    }

    private void loadRepos() {
        subscription = AppObservable.bindActivity(this, reposLoader.loadRepos("EgorAnd")
                .subscribeOn(Schedulers.io()))
                .subscribe(
                        repos -> {
                            progress.setVisibility(View.INVISIBLE);
                            reposRecyclerView.setAdapter(new ReposAdapter(mainActivityComponent.layoutInflater(), repos));
                        },
                        throwable -> {
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                            Log.e(MainActivity.class.getSimpleName(), throwable.getLocalizedMessage(), throwable);
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
