package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Response;

public interface MainListContract {

    interface PresenterInterface{
        void getHttpData();
        void getRoomData();
        void setHttpToRoom(List<GitResult> list);
        void clearRoom();
        void onRefresh();
    }

    interface MainViewInterface {
        void showProgress(boolean show);
        void setDataToListView(Response<List<GitResult>> response);
        void setDataToListView(List<GitResult> response);
        void setEmptyData();
        void showToast(String message);
    }

    interface MainListIntactor extends CoreIntractor{
        Observable<Response<List<GitResult>>> getHttpGitProjects(int page);
        Flowable<List<GitResult>> getRoomProjects();
        void addRoomProjects(List<GitResult> list);
    }
}
