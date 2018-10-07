package com.home.joseki.repositoryviewer.dal;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.enums.RoomTableEnum;
import com.home.joseki.repositoryviewer.interfaces.CommitsContract;
import com.home.joseki.repositoryviewer.interfaces.ContributorContract;
import com.home.joseki.repositoryviewer.interfaces.DaoGitResult;
import com.home.joseki.repositoryviewer.interfaces.IGitMassage;
import com.home.joseki.repositoryviewer.interfaces.MainListContract;
import com.home.joseki.repositoryviewer.interfaces.ProjectInfoContract;
import com.home.joseki.repositoryviewer.models.Commits;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//https://api.github.com/orgs/square/repos
public class GitIntractor implements MainListContract.MainListIntactor, CommitsContract.CommitsIntactor, ContributorContract.ContributorIntactor, ProjectInfoContract.ProjectInfoIntactor {
    private Activity activity;

    private IGitMassage messagesApi;
    private AppDatabase db;

    @Database(entities = {GitResult.class, Contributors.class, Commits.class}, version = 1, exportSchema = false)
    abstract static class AppDatabase extends RoomDatabase {
        abstract DaoGitResult resultDao();
    }

    public GitIntractor(Context context, Activity a){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        activity = a;

        messagesApi = retrofit.create(IGitMassage.class);

        db =  Room.databaseBuilder(context,
                AppDatabase.class, "gitbase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    public Observable<Response<List<GitResult>>> getHttpGitProjects(int page) {
        return messagesApi.getRepositoryMessage(String.valueOf(page));
    }

    @Override
    public Observable<Response<List<Commits>>> getHttpCommitsProjects(String repo, int page) {
        return messagesApi.getCommitsMessage(repo, String.valueOf(page));
    }

    @Override
    public Flowable<List<Commits>> getRoomCommitsByUrl(String url) {
        return  db.resultDao().getCommitsByUrl(url);
    }

    @Override
    public Observable<Response<List<Contributors>>> getHttpGitContributors(String repo) {
        return messagesApi.getContributorsMessage(repo);
    }

    @Override
    public Flowable<List<Contributors>> getRoomContributorsByUrl(String url) {
        return  db.resultDao().getContributorsByUrl(url);
    }

    @Override
    public long addRoomContributors(final List<Contributors> list) {
        io.reactivex.Completable.fromAction(new Action() {
            @Override
            public void run() {
                db.resultDao().insertContributors(list);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        return 0;
    }

    @Override
    public Flowable<List<GitResult>> getRoomProjects(){
        return  db.resultDao().getAllProjects();
    }

    @Override
    public long addRoomCommits(final List<Commits> list) {
        io.reactivex.Completable.fromAction(new Action() {
            @Override
            public void run() {
                db.resultDao().insertCommits(list);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        return 0;
    }

    @Override
    public void addRoomProjects(final List<GitResult> list) {
        io.reactivex.Completable.fromAction(new Action() {
            @Override
            public void run() {
                db.resultDao().insertProjects(list);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void clearRoom(RoomTableEnum table, String url) {
        switch (table){
            case COMMITS:
                db.resultDao().clearCommitsByUrl(url);
            break;
            case PROJECTS:
                db.resultDao().clearProjectTable();
            break;
            case CONTRIBUTORS:
                db.resultDao().clearContributorsByUrl(url);
            break;
        }
    }

    @Override
    public void setIntToPreferences(String key, int value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public int getIntFromPreferences(String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 1);
    }

    @Override
    public String getResourceString(int id) {
        if (activity == null){
            return "";
        } else {
            return activity.getString(id);
        }
    }

    @Override
    public Single<Boolean> isOnline() {
        /*return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call(){
                try {
                    ConnectivityManager cm =
                            (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(cm != null) {
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        return activeNetwork.isConnectedOrConnecting();
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());*/

        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                try {
                    int timeoutMs = 1500;
                    Socket socket = new Socket();
                    InetSocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

                    socket.connect(socketAddress, timeoutMs);
                    socket.close();

                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
