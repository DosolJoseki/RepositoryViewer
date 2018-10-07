package com.home.joseki.repositoryviewer.presenters;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.interfaces.ProjectInfoContract;
import com.home.joseki.repositoryviewer.models.Contributors;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProjectInfoPresenter implements ProjectInfoContract.PresenterInterface {

    private ProjectInfoContract.ProjectInfoViewInterface view = null;
    private ProjectInfoContract.ProjectInfoIntactor noticeIntactor = null;

    public ProjectInfoPresenter(ProjectInfoContract.ProjectInfoViewInterface mvi, ProjectInfoContract.ProjectInfoIntactor gni){
        view = mvi;
        noticeIntactor = gni;
    }

    @Override
    public void getHttpData(final String repo) {
        if(view != null){
            view.showProgress(true);
        } else {
            return;
        }

        noticeIntactor.isOnline().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean){
                    noticeIntactor
                            .getHttpGitContributors(repo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(1)
                            .subscribe(new Observer<Response<List<Contributors>>>() {
                                           @Override
                                           public void onSubscribe(Disposable d) {
                                               view.showProgress(true);
                                           }

                                           @Override
                                           public void onNext(Response<List<Contributors>> gitResults) {
                                               if(gitResults == null || gitResults.body() == null){
                                                   if(gitResults.errorBody() != null){
                                                       try {
                                                           view.showToast(gitResults.errorBody().string());
                                                       } catch (Exception ex){
                                                           view.showToast(noticeIntactor.getResourceString(R.string.error_string_http_empty));
                                                       }
                                                   } else {
                                                       view.showToast(noticeIntactor.getResourceString(R.string.error_string_http_empty));
                                                   }
                                               } else {
                                                   if(view != null){
                                                       if(gitResults.body().isEmpty()){
                                                           return;
                                                       }
                                                       view.setDataToListView(gitResults);
                                                   }
                                               }
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               if(view != null){
                                                   view.showToast(e.getMessage());
                                               }
                                           }

                                           @Override
                                           public void onComplete() {
                                               if(view != null){
                                                   view.showProgress(false);
                                               }
                                           }
                                       }
                            );
                } else {
                    view.showToast(noticeIntactor.getResourceString(R.string.error_string_no_internet_connection));
                }
            }
        });
    }
}
