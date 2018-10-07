package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.models.Contributors;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Response;

public interface ContributorContract {

    interface PresenterInterface {
        void getHttpData();
        void getRoomData();
        void setHttpToRoom(List<Contributors> list);
        void clearRoom();
        void onRefresh();
    }

    interface ContributorViewInterface {
        void showProgress(boolean show);
        void setDataToListView(Response<List<Contributors>> response);
        void setDataToListView(List<Contributors> response);
        void setEmptyData();
        void showToast(String message);
        void setAppName(String name);
    }

    interface ContributorIntactor extends CoreIntractor{
        Observable<Response<List<Contributors>>> getHttpGitContributors(String repo);
        Flowable<List<Contributors>> getRoomContributorsByUrl(String url);
        long addRoomContributors(List<Contributors> list);
    }
}
