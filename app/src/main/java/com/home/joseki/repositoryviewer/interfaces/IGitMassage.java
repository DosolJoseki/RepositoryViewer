package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.models.Commits;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IGitMassage {

    @GET("/orgs/square/repos?per_page=100&type=owner")
    Observable<Response<List<GitResult>>> getRepositoryMessage(@Query("page") String page);

    @GET("/repos/square/{repo}/stats/contributors")
    Observable<Response<List<Contributors>>> getContributorsMessage(@Path("repo") String repository);

    @GET("/repos/square/{repo}/commits")
    Observable<Response<List<Commits>>> getCommitsMessage(@Path("repo") String repository, @Query("page") String page);
}
