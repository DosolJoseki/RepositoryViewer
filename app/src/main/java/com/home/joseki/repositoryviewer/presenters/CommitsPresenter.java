package com.home.joseki.repositoryviewer.presenters;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.enums.RoomTableEnum;
import com.home.joseki.repositoryviewer.interfaces.CommitsContract;
import com.home.joseki.repositoryviewer.models.Commits;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class CommitsPresenter implements CommitsContract.PresenterInterface {
    private CommitsContract.CommitsViewInterface view = null;
    private CommitsContract.CommitsIntactor noticeIntactor = null;

    private boolean isRefreshing = false;
    private int currentPage = 1;
    private GitResult project = null;

    public CommitsPresenter(CommitsContract.CommitsViewInterface mvi, CommitsContract.CommitsIntactor gni, GitResult result){
        view = mvi;
        noticeIntactor = gni;
        project = result;

        view.setProjName(result.getName());
        currentPage = noticeIntactor.getIntFromPreferences(noticeIntactor.getResourceString(R.string.preferences_last_page_commits));
    }

    @Override
    public void getHttpData() {
        if(view != null){
            view.showProgress(true);
        }

        noticeIntactor.isOnline().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean){
                    noticeIntactor
                            .getHttpCommitsProjects(project.getName(), currentPage)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(1)
                            .subscribe(new Observer<Response<List<Commits>>>() {
                                           @Override
                                           public void onSubscribe(Disposable d) {
                                               view.showProgress(true);
                                           }

                                           @Override
                                           public void onNext(Response<List<Commits>> gitResults) {
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
                                                       currentPage++;
                                                       if(isRefreshing){
                                                           view.setEmptyData();
                                                       }
                                                       noticeIntactor.setIntToPreferences(noticeIntactor.getResourceString(R.string.preferences_last_page_main), currentPage);
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
                                           }
                                       }
                            );
                } else {
                    view.showToast(noticeIntactor.getResourceString(R.string.error_string_no_internet_connection));
                }
            }
        });
    }

    @Override
    public void getRoomData() {
        noticeIntactor
                .getRoomCommits(project.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Consumer<List<Commits>>() {
                    @Override
                    public void accept(List<Commits> gitResults) {
                        if (gitResults.size() == 0){
                            getHttpData();
                        } else {
                            view.setDataToListView(gitResults);
                        }
                    }
                });
    }

    @Override
    public void setHttpToRoom(List<Commits> list) {
        noticeIntactor.addRoomCommits(list);
    }

    @Override
    public void clearRoom() {
        noticeIntactor.clearRoom(RoomTableEnum.COMMITS, project.getUrl());
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        isRefreshing = true;
        clearRoom();
        getHttpData();
    }
}
