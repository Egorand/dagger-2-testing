package me.egorand.dagger_2_testing.data;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import me.egorand.dagger_2_testing.BuildConfig;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ReposDiskDatastoreTest {

    private ReposDiskDatastore datastore;

    private SQLiteDatabase mockReadableDatabase;
    private SQLiteDatabase mockWritableDatabase;

    @Before
    public void setUp() {
        ReposDatabaseHelper mockDatabaseHelper = mock(ReposDatabaseHelper.class);
        mockReadableDatabase = mock(SQLiteDatabase.class);
        mockWritableDatabase = mock(SQLiteDatabase.class);

        when(mockDatabaseHelper.getReadableDatabase()).thenReturn(mockReadableDatabase);
        when(mockDatabaseHelper.getWritableDatabase()).thenReturn(mockWritableDatabase);

        datastore = new ReposDiskDatastore(mockDatabaseHelper);
    }

    @Test
    public void dataSavedProperly() {
        datastore.save(TestData.REPOS);

        for (Repo repo : TestData.REPOS) {
            verify(mockWritableDatabase).insert(Repo.TABLE_NAME, null, repo.toContentValues());
        }
        verify(mockWritableDatabase).close();
    }

    @Test
    public void queryingEmptyDatastoreYieldsEmptyObservable() {
        Cursor emptyCursor = withValues(Collections.emptyList());
        when(mockReadableDatabase.rawQuery("SELECT * FROM " + Repo.TABLE_NAME, null))
                .thenReturn(emptyCursor);
        TestSubscriber<List<Repo>> testSubscriber = new TestSubscriber<>();

        datastore.queryAll().subscribe(testSubscriber);

        testSubscriber.assertNoValues();
    }

    @Test
    public void queryingFullDatastoreYieldsProperData() {
        Cursor cursor = withValues(TestData.REPOS);
        when(mockReadableDatabase.rawQuery("SELECT * FROM " + Repo.TABLE_NAME, null))
                .thenReturn(cursor);
        TestSubscriber<List<Repo>> testSubscriber = new TestSubscriber<>();

        datastore.queryAll().subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(TestData.REPOS));
    }

    @Test
    public void dataClearedProperly() {
        datastore.clear();

        verify(mockWritableDatabase).delete(Repo.TABLE_NAME, null, null);
        verify(mockWritableDatabase).close();
    }

    private Cursor withValues(List<Repo> repos) {
        MatrixCursor cursor = new MatrixCursor(new String[]{Repo.COLUMN_ID, Repo.COLUMN_NAME,
                Repo.COLUMN_DESCRIPTION});
        for (Repo repo : repos) {
            cursor.newRow().add(repo.id).add(repo.name).add(repo.description);
        }
        return cursor;
    }
}