package com.home.joseki.repositoryviewer.dal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.interfaces.MainContract;
import com.home.joseki.repositoryviewer.models.GitResult;

public class ActivityIntractor implements MainContract.IActivityIntactor {
    private AppCompatActivity activity = null;

    public ActivityIntractor(AppCompatActivity a){
        activity = a;
    }

    @Override
    public Bundle setBundle(GitResult gitResult) {
        Gson gson = new Gson();

        Bundle bundle = new Bundle();
        bundle.putString(activity.getString(R.string.git_res_instance), gson.toJson(gitResult));
        return bundle;
    }

    @Override
    public int getBackCount() {
        return activity.getFragmentManager().getBackStackEntryCount();
    }
}
