package com.home.joseki.repositoryviewer;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.home.joseki.repositoryviewer.dal.ActivityIntractor;
import com.home.joseki.repositoryviewer.fargments.CommitsFragment;
import com.home.joseki.repositoryviewer.fargments.ContributorsFragment;
import com.home.joseki.repositoryviewer.fargments.MainListFragment;
import com.home.joseki.repositoryviewer.fargments.ProjectInfoFragment;
import com.home.joseki.repositoryviewer.interfaces.MainContract;
import com.home.joseki.repositoryviewer.models.GitResult;

public class MainActivity extends AppCompatActivity implements MainContract.ActivityViewInterface {
    MainContract.PresenterInterface presenter;

    MainListFragment mainListFragment;
    CommitsFragment commitsFragment;
    ContributorsFragment contributionsFragment;
    ProjectInfoFragment projectInfoFragment;
    FragmentTransaction fTrans;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListFragment = new MainListFragment();
        commitsFragment = new CommitsFragment();
        contributionsFragment = new ContributorsFragment();
        projectInfoFragment = new ProjectInfoFragment();

        presenter = new MainPresenter(this, new ActivityIntractor(this));
        presenter.setFragmentMainList();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void onPopBackStack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressedCore() {
        super.onBackPressed();
    }

    @Override
    public void getFragmentCommits(GitResult gitResult) {
        presenter.setFragmentCommits(gitResult);
    }

    @Override
    public void getFragmentContributors(GitResult gitResult) {
        presenter.setFragmentContributors(gitResult);
    }

    @Override
    public void getFragmentProjectInfo(GitResult gitResult) {
        presenter.setFragmentProjectInfo(gitResult);
    }

    @Override
    public void setFragmentMainList() {
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.frameLayout, mainListFragment, getString(R.string.fragment_main_tag));
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void setFragmentCommits(Bundle bundle) {
        commitsFragment.setArguments(bundle);

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.hide(projectInfoFragment);
        fTrans.add(R.id.frameLayout, commitsFragment, getString(R.string.fragment_commits_tag));
        fTrans.addToBackStack(getString(R.string.fragment_commits_tag));
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void setFragmentContributors(Bundle bundle) {
        contributionsFragment.setArguments(bundle);

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.hide(projectInfoFragment);
        fTrans.add(R.id.frameLayout, contributionsFragment, getString(R.string.fragment_contributors_tag));
        fTrans.addToBackStack(getString(R.string.fragment_contributors_tag));
        fTrans.commitAllowingStateLoss();
    }

    @Override
    public void setFragmentProjectInfo(Bundle bundle) {
        projectInfoFragment.setArguments(bundle);

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.hide(mainListFragment);
        fTrans.add(R.id.frameLayout, projectInfoFragment, getString(R.string.fragment_info_tag));
        fTrans.addToBackStack(getString(R.string.fragment_info_tag));
        fTrans.commitAllowingStateLoss();
    }
}
