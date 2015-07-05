package me.egorand.dagger_2_testing;

import java.util.Arrays;
import java.util.List;

import me.egorand.dagger_2_testing.data.Repo;

public interface TestData {

    List<Repo> REPOS = Arrays.asList(
            new Repo(1L, "Repo 1", "This is repo #1"),
            new Repo(2L, "Repo 2", "This is repo #2"),
            new Repo(3L, "Repo 3", "This is repo #3"),
            new Repo(4L, "Repo 4", "This is repo #4"),
            new Repo(5L, "Repo 5", "This is repo #5")
    );
    String USERNAME = "test";
}
