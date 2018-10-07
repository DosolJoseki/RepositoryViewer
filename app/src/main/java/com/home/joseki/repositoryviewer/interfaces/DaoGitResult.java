package com.home.joseki.repositoryviewer.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.home.joseki.repositoryviewer.models.Commits;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DaoGitResult {

    @Query("SELECT * FROM GitResult")
    Flowable<List<GitResult>> getAllProjects();

    @Query("SELECT * FROM Commits")
    Flowable<List<Commits>> getAllCommits();

    @Query("SELECT * FROM Contributors")
    Flowable<List<Contributors>> getAllContributors();

    @Query("DELETE FROM GitResult")
    void clearProjectTable();

    @Query("DELETE FROM Commits")
    void clearCommitsTable();

    @Query("DELETE FROM Contributors")
    void clearContributorsTable();

    @Query("DELETE FROM Commits WHERE url LIKE :url")
    void clearCommitsByUrl(String url);

    @Query("DELETE FROM Contributors WHERE url = :url")
    void clearContributorsByUrl(String url);

    @Query("SELECT * FROM GitResult WHERE gitresult_name = :name")
    Flowable<List<GitResult>> getByName(String name);

    @Query("SELECT * FROM Commits WHERE url LIKE :url")
    Flowable<List<Commits>> getCommitsByUrl(String url);

    @Query("SELECT * FROM Contributors WHERE url = :url")
    Flowable<List<Contributors>> getContributorsByUrl(String url);

    @Insert
    void insertProjects(List<GitResult> list);

    @Insert
    void insertCommits(List<Commits> list);

    @Insert
    void insertContributors(List<Contributors> list);

    @Update
    void update(GitResult result);

    @Delete
    void delete(GitResult result);

}
