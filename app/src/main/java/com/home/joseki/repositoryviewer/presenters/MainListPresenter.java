package com.home.joseki.repositoryviewer.presenters;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.enums.RoomTableEnum;
import com.home.joseki.repositoryviewer.interfaces.MainListContract;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class MainListPresenter implements MainListContract.PresenterInterface {

    private MainListContract.MainViewInterface mainView = null;
    private MainListContract.MainListIntactor noticeIntactor = null;
    private int currentPage = 1;
    private boolean isRefreshing = false;

    public MainListPresenter(MainListContract.MainViewInterface mvi, MainListContract.MainListIntactor gni){
        mainView = mvi;
        noticeIntactor = gni;
        currentPage =  noticeIntactor.getIntFromPreferences(noticeIntactor.getResourceString(R.string.preferences_last_page_main));
    }

    @Override
    public void getHttpData() {
        if(mainView != null){
            mainView.showProgress(true);
        }

        noticeIntactor.isOnline().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean){
                    noticeIntactor
                            .getHttpGitProjects(currentPage)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(1)
                            .subscribe(new Observer<Response<List<GitResult>>>() {
                                           @Override
                                           public void onSubscribe(Disposable d) {
                                               mainView.showProgress(true);
                                           }

                                           @Override
                                           public void onNext(Response<List<GitResult>> gitResults) {
                                               if(gitResults == null || gitResults.body() == null){
                                                   if(gitResults.errorBody() != null){
                                                       try {
                                                           mainView.showToast(gitResults.errorBody().string());
                                                       } catch (Exception ex){
                                                           mainView.showToast(noticeIntactor.getResourceString(R.string.error_string_http_empty));
                                                       }
                                                   } else {
                                                       mainView.showToast(noticeIntactor.getResourceString(R.string.error_string_http_empty));
                                                   }
                                               } else {
                                                   if(mainView != null){
                                                       if(gitResults.body().isEmpty()){
                                                           return;
                                                       }
                                                       currentPage++;
                                                       if(isRefreshing){
                                                           mainView.setEmptyData();
                                                       }
                                                       noticeIntactor.setIntToPreferences(noticeIntactor.getResourceString(R.string.preferences_last_page_main), currentPage);
                                                       mainView.setDataToListView(gitResults);
                                                       setHttpToRoom(gitResults.body());
                                                   }
                                               }
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               if(mainView != null){
                                                   mainView.showToast(e.getMessage());
                                               }
                                           }

                                           @Override
                                           public void onComplete() {
                                               isRefreshing = false;
                                               if(mainView != null){
                                                   mainView.showProgress(false);
                                               }
                                           }
                                       }
                            );
                } else {
                    mainView.showToast(noticeIntactor.getResourceString(R.string.error_string_no_internet_connection));
                }
            }
        });
    }

    @Override
    public void getRoomData() {
        noticeIntactor
                .getRoomProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(new Consumer<List<GitResult>>() {
                    @Override
                    public void accept(List<GitResult> gitResults) {
                        if (gitResults.size() == 0){
                            getHttpData();
                        } else {
                            mainView.setDataToListView(gitResults);
                        }
                    }
                });
    }

    @Override
    public void setHttpToRoom(List<GitResult> list) {
        noticeIntactor.addRoomProjects(list);
    }

    @Override
    public void clearRoom() {
        noticeIntactor.clearRoom(RoomTableEnum.PROJECTS, null);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        isRefreshing = true;
        clearRoom();
        getHttpData();
    }
}
