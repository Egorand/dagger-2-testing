package me.egorand.dagger_2_testing;

import javax.inject.Inject;

import me.egorand.dagger_2_testing.data.Datastore;
import me.egorand.dagger_2_testing.data.Repo;
import me.egorand.dagger_2_testing.di.qualifiers.Disk;

public class DiskDataLoadingTest extends BaseDataLoadingTest {

    @Inject @Disk Datastore<Repo> datastore;

    @Override
    protected void injectChild(TestAppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void setUp() {
        super.setUp();
        datastore.save(TestData.REPOS);
    }
}
