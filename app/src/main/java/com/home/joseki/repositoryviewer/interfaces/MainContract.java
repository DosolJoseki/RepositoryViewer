package com.home.joseki.repositoryviewer.interfaces;

import android.os.Bundle;

import com.home.joseki.repositoryviewer.models.GitResult;

public interface MainContract {
    interface PresenterInterface{
        void setFragmentMainList();
        void setFragmentCommits(GitResult gitResult);
        void setFragmentContributors(GitResult gitResult);
        void setFragmentProjectInfo(GitResult gitResult);
        void onBackPressed();
    }

    interface ActivityViewInterface {
        void setFragmentMainList();
        void setFragmentCommits(Bundle bundle);
        void setFragmentContributors(Bundle bundle);
        void setFragmentProjectInfo(Bundle bundle);
        void onBackPressed();
        void onPopBackStack();
        void onBackPressedCore();

        void getFragmentCommits(GitResult gitResult);
        void getFragmentContributors(GitResult gitResult);
        void getFragmentProjectInfo(GitResult gitResult);

    }

    interface IActivityIntactor{
        Bundle setBundle(GitResult gitResult);
        int getBackCount();
    }
}
