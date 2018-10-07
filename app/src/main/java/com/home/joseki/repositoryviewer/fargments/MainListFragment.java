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

import com.home.joseki.repositoryviewer.MainActivity;
import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.adapters.ProjectsAdapter;
import com.home.joseki.repositoryviewer.dal.GitIntractor;
import com.home.joseki.repositoryviewer.interfaces.IProjectAdapterListener;
import com.home.joseki.repositoryviewer.interfaces.MainListContract;
import com.home.joseki.repositoryviewer.models.GitResult;
import com.home.joseki.repositoryviewer.presenters.MainListPresenter;

import java.util.List;

import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class MainListFragment extends Fragment implements MainListContract.MainViewInterface {
    ProjectsAdapter projectsAdapter = null;
    RecyclerView projListView = null;

    private MainActivity activity = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private MainListContract.PresenterInterface presenter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        activity = (MainActivity)getActivity();
        projectsAdapter = new ProjectsAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        swipeRefreshLayout = activity.findViewById(R.id.srlProj);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        projListView = activity.findViewById(R.id.rvProjectList);
        projListView.setLayoutManager(layoutManager);
        projListView.setAdapter(projectsAdapter);

        TextView appName = activity.findViewById(R.id.tv_fm_proj_name);
        appName.setText(getString(R.string.app_name));

        assert context != null;
        presenter = new MainListPresenter(this, new GitIntractor(context, activity));

        projectsAdapter.setListeners(new IProjectAdapterListener() {
            @Override
            public void onBottomReached() {
                presenter.getHttpData();
            }
        });

        projectsAdapter.addSubscribe(new Consumer<GitResult>() {
            @Override
            public void accept(GitResult gitResult) {
                activity.getFragmentProjectInfo(gitResult);
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
    public void setDataToListView(Response<List<GitResult>> response){
        if(projectsAdapter != null){
            projectsAdapter.setItems(response.body());
        }
    }

    @Override
    public void setDataToListView(List<GitResult> response) {
        projectsAdapter.setItems(response);
    }

    @Override
    public void setEmptyData() {
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if(projectsAdapter != null){
            projectsAdapter.clearItems();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
