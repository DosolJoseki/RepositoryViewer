package com.home.joseki.repositoryviewer.fargments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.home.joseki.repositoryviewer.MainActivity;
import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.adapters.CommitsAdapter;
import com.home.joseki.repositoryviewer.dal.GitIntractor;
import com.home.joseki.repositoryviewer.interfaces.CommitsContract;
import com.home.joseki.repositoryviewer.interfaces.ICommitAdapterListener;
import com.home.joseki.repositoryviewer.models.Commits;
import com.home.joseki.repositoryviewer.models.GitResult;
import com.home.joseki.repositoryviewer.presenters.CommitsPresenter;

import java.util.List;

import retrofit2.Response;

public class CommitsFragment extends Fragment implements CommitsContract.CommitsViewInterface {
    CommitsAdapter commitsAdapter = null;
    RecyclerView commitListView = null;

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private CommitsContract.PresenterInterface presenter = null;
    private TextView appName = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commits, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Context context = getContext();
        MainActivity activity = (MainActivity) getActivity();
        commitsAdapter = new CommitsAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        assert activity != null;
        swipeRefreshLayout = activity.findViewById(R.id.srlCommit);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        appName = activity.findViewById(R.id.tv_comm_proj_name);

        commitListView = activity.findViewById(R.id.rv_contr_contribute_list);
        commitListView.setLayoutManager(layoutManager);
        commitListView.setAdapter(commitsAdapter);

        Gson gson = new Gson();
        Bundle bundle = getArguments();
        assert bundle != null;
        GitResult result = gson.fromJson(bundle.getString(getString(R.string.git_res_instance)), GitResult.class);

        assert context != null;
        presenter = new CommitsPresenter(this, new GitIntractor(context, activity), result);

        commitsAdapter.setListeners(new ICommitAdapterListener() {
            @Override
            public void onBottomReached() {
                presenter.getHttpData();
            }
        });

        presenter.getRoomData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress(final boolean show) {
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(show);
        }
    }

    @Override
    public void setDataToListView(Response<List<Commits>> response) {
        if(commitsAdapter != null){
            commitsAdapter.setItems(response.body());
        }
    }

    @Override
    public void setDataToListView(List<Commits> response) {
        if(commitsAdapter != null){
            commitsAdapter.setItems(response);
        }
    }

    @Override
    public void setEmptyData() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setProjName(String name) {
        appName.setText(name);
    }
}
