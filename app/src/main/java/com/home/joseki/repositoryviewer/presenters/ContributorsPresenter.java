package com.home.joseki.repositoryviewer.presenters;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.enums.RoomTableEnum;
import com.home.joseki.repositoryviewer.interfaces.ContributorContract;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ContributorsPresenter implements ContributorContract.PresenterInterface {
    private ContributorContract.ContributorViewInterface view = null;
    private ContributorContract.ContributorIntactor noticeIntactor = null;

    private GitResult project = null;
    private boolean isRefreshing = false;

    public ContributorsPresenter(ContributorContract.ContributorViewInterface mvi, ContributorContract.ContributorIntactor gni, GitResult result){
        view = mvi;
        noticeIntactor = gni;
        project = result;

        view.setAppName(project.getName());
    }

    @Override
    public void getHttpData() {
        if(view != null){
            view.showProgress(true);
        }
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(noticeIntactor.isOnline().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean){
                    noticeIntactor
                            .getHttpGitContributors(project.getName())
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
                                                       if(isRefreshing){
                                                           view.setEmptyData();
                                                       }
                                                       view.setDataToListView(gitResults);
                                                       setHttpToRoom(gitResults.body());
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
                                               isRefreshing = false;
                                               if(view != null){
                                                   view.showProgress(false);
                                               }
                                               compositeDisposable.dispose();
                                           }
                                       }
                            );
                } else {
                    view.showToast(noticeIntactor.getResourceString(R.string.error_string_no_internet_connection));
                    compositeDisposable.dispose();
                }
            }
        }));
    }

    @Override
    public void getRoomData() {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(noticeIntactor
                .getRoomContributorsByUrl(project.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Consumer<List<Contributors>>() {
                    @Override
                    public void accept(List<Contributors> gitResults) {
                        if (gitResults.size() == 0){
                            getHttpData();
                        } else {
                            view.setDataToListView(gitResults);
                        }
                        compositeDisposable.dispose();
                    }
                }));
    }

    @Override
    public void setHttpToRoom(List<Contributors> list) {
        for(Contributors contributor: list){
            contributor.setUrl(project.getUrl());
        }
        long i = noticeIntactor.addRoomContributors(list);
    }

    @Override
    public void clearRoom() {
        noticeIntactor.clearRoom(RoomTableEnum.CONTRIBUTORS, project.getUrl());
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        clearRoom();
        getHttpData();
    }
}
