package me.egorand.dagger_2_testing.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ReposDiskDatastore implements Datastore<Repo> {

    private final ReposDatabaseHelper databaseHelper;

    public ReposDiskDatastore(ReposDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void save(List<Repo> data) {
        try {
            for (Repo repo : data) {
                databaseHelper.getWritableDatabase().insert(Repo.TABLE_NAME, null, repo.toContentValues());
            }
        } finally {
            databaseHelper.getWritableDatabase().close();
        }
    }

    @Override
    public Observable<List<Repo>> queryAll() {
        Cursor cursor = null;
        try {
            cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Repo.TABLE_NAME, null);
            if (cursor.getCount() == 0) {
                return Observable.empty();
            }
            List<Repo> repos = new ArrayList<>();
            while (cursor.moveToNext()) {
                repos.add(Repo.fromCursor(cursor));
            }
            return Observable.just(repos);
        } finally {
            databaseHelper.getReadableDatabase().close();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void clear() {
        try {
            databaseHelper.getWritableDatabase().delete(Repo.TABLE_NAME, null, null);
        } finally {
            databaseHelper.getWritableDatabase().close();
        }
    }
}
