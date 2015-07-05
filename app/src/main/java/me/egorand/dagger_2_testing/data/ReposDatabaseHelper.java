package me.egorand.dagger_2_testing.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

@Singleton
public class ReposDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "repos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Repo.TABLE_NAME + " (" +
                    Repo.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    Repo.COLUMN_NAME + " TEXT, " +
                    Repo.COLUMN_DESCRIPTION + " TEXT);";
    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + Repo.TABLE_NAME;

    public ReposDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
