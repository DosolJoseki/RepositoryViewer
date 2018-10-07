package com.home.joseki.repositoryviewer;

import com.home.joseki.repositoryviewer.interfaces.MainContract;
import com.home.joseki.repositoryviewer.models.GitResult;

public class MainPresenter implements MainContract.PresenterInterface {
    private MainContract.ActivityViewInterface view = null;
    private MainContract.IActivityIntactor intractor = null;

    public MainPresenter(MainContract.ActivityViewInterface avi, MainContract.IActivityIntactor i){
        view = avi;
        intractor = i;
    }

    @Override
    public void setFragmentMainList() {
        view.setFragmentMainList();
    }

    @Override
    public void setFragmentCommits(GitResult gitResult) {
        view.setFragmentCommits(intractor.setBundle(gitResult));
    }

    @Override
    public void setFragmentContributors(GitResult gitResult) {
        view.setFragmentContributors(intractor.setBundle(gitResult));
    }

    @Override
    public void setFragmentProjectInfo(GitResult gitResult) {
        view.setFragmentProjectInfo(intractor.setBundle(gitResult));
    }

    @Override
    public void onBackPressed() {
        int count = intractor.getBackCount();

        if (count == 0) {
            view.onBackPressedCore();
        } else {
            view.onPopBackStack();
        }
    }
}
