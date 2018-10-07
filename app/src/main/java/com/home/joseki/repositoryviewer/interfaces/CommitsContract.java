package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.models.Commits;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Response;


public interface CommitsContract {
    interface PresenterInterface{
        void getHttpData();
        void getRoomData();
        void setHttpToRoom(List<Commits> list);
        void clearRoom();
        void onRefresh();
    }

    interface CommitsViewInterface {
        void showProgress(boolean show);
        void setDataToListView(Response<List<Commits>> response);
        void setDataToListView(List<Commits> response);
        void setEmptyData();
        void showToast(String message);
        void setProjName(String name);
    }

    interface CommitsIntactor extends CoreIntractor{
        Observable<Response<List<Commits>>> getHttpCommitsProjects(String repo, int page);
        Flowable<List<Commits>> getRoomCommitsByUrl(String url);
        long addRoomCommits(List<Commits> list);
    }
}
