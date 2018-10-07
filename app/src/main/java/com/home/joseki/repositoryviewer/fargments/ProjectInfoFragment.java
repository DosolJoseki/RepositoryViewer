package com.home.joseki.repositoryviewer.fargments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.home.joseki.repositoryviewer.MainActivity;
import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.dal.GitIntractor;
import com.home.joseki.repositoryviewer.interfaces.ProjectInfoContract;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.home.joseki.repositoryviewer.models.GitResult;
import com.home.joseki.repositoryviewer.presenters.ProjectInfoPresenter;

import java.util.List;

import retrofit2.Response;

public class ProjectInfoFragment extends Fragment implements ProjectInfoContract.ProjectInfoViewInterface {

    private Context context = null;
    private TextView projectDecs;
    private TextView projectName;
    private TextView projectStars;
    private TextView projectsContributors;
    private Button btnCommits;
    private Button btnContributes;
    private MainActivity activity;
    private ProjectInfoContract.PresenterInterface presenter = null;
    private int errorCount = 0;
    private GitResult result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_info, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity = (MainActivity)getActivity();
        context = getContext();

        Gson gson = new Gson();
        Bundle bundle = getArguments();
        result = gson.fromJson(bundle.getString(getString(R.string.git_res_instance)), GitResult.class);

        projectDecs = (TextView)getActivity().findViewById(R.id.tv_fpi_proj_desc);
        projectName = (TextView)getActivity().findViewById(R.id.tv_fpi_proj_name);
        projectStars = (TextView)getActivity().findViewById(R.id.tv_fpi_stars);
        projectsContributors = (TextView)getActivity().findViewById(R.id.tv_fpi_contributors);

        projectStars.setText(result.getStargazers_count());

        btnCommits = (Button)getActivity().findViewById(R.id.btn_fpi_commits);
        btnContributes = (Button)getActivity().findViewById(R.id.btn_fpi_contributes);

        btnCommits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getFragmentCommits(result);
            }
        });

        btnContributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getFragmentContributors(result);
            }
        });

        presenter = new ProjectInfoPresenter(this, new GitIntractor(context, activity));

        presenter.getHttpData(result.getName());

        setProjProps(result);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void setDataToListView(Response<List<Contributors>> response) {
        if(response.body() == null){
            if(response.errorBody() != null){
                try {
                    Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                } catch (Exception ex){
                    Toast.makeText(getContext(), getString(R.string.error_string_http_empty), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.error_string_http_empty), Toast.LENGTH_LONG).show();
            }
            return;
        }

        errorCount = 0;
        if(response.body().size() > 0) {
            fillInformation(response);
        }
    }

    @Override
    public void setEmptyData() {

    }

    @Override
    public void setContributors(String i) {

    }

    private void fillInformation(Response<List<Contributors>> list) {
        if(list != null && list.body() != null) {
            projectsContributors.setText(String.valueOf(list.body().size()));
        } else {
            projectsContributors.setText("-");
        }
    }

    @Override
    public void showToast(String message) {
        errorCount++;
        if(errorCount > 3) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            presenter.getHttpData(result.getName());
        }
    }

    private void setProjProps(GitResult result){
        if(result == null){
            return;
        }

        projectDecs.setText(result.getDescription());
        projectName.setText(result.getName());
    }
}
