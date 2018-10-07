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
import com.home.joseki.repositoryviewer.adapters.ContributorsAdapter;
import com.home.joseki.repositoryviewer.dal.GitIntractor;
import com.home.joseki.repositoryviewer.interfaces.ContributorContract;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;
import com.home.joseki.repositoryviewer.presenters.ContributorsPresenter;

import java.util.List;

import retrofit2.Response;

public class ContributorsFragment extends Fragment implements ContributorContract.ContributorViewInterface  {
    ContributorsAdapter contributorsAdapter = null;
    RecyclerView contrListView = null;

    private SwipeRefreshLayout swipeRefreshLayout = null;
    private ContributorContract.PresenterInterface presenter = null;
    private TextView appName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contributes, null);
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
        contributorsAdapter = new ContributorsAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        assert activity != null;
        swipeRefreshLayout = activity.findViewById(R.id.srlContr);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getHttpData();
            }
        });

        contrListView = activity.findViewById(R.id.rv_fc_contribute_list);
        contrListView.setLayoutManager(layoutManager);
        contrListView.setAdapter(contributorsAdapter);

        appName = activity.findViewById(R.id.tv_fc_proj_name);

        Gson gson = new Gson();
        Bundle bundle = getArguments();
        assert bundle != null;
        GitResult result = gson.fromJson(bundle.getString(getString(R.string.git_res_instance)), GitResult.class);

        assert context != null;
        presenter = new ContributorsPresenter(this, new GitIntractor(context, activity), result);

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
    public void setDataToListView(Response<List<Contributors>> response) {
        contributorsAdapter.setItems(response.body());
    }

    @Override
    public void setDataToListView(List<Contributors> response) {
        contributorsAdapter.setItems(response);
    }

    @Override
    public void setEmptyData() {
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if(contributorsAdapter != null){
            contributorsAdapter.clearItems();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAppName(String name) {
        appName.setText(name);
    }
}
