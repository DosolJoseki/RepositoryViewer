package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.models.Contributors;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface ProjectInfoContract {
    interface PresenterInterface{
        void getHttpData(String repo);
    }

    interface ProjectInfoViewInterface {
        void showProgress(boolean show);
        void setDataToListView(Response<List<Contributors>> response);
        void setEmptyData();
        void setContributors(String i);
        void showToast(String message);
    }

    interface ProjectInfoIntactor extends CoreIntractor{
        Observable<Response<List<Contributors>>> getHttpGitContributors(String repo);
    }
}
