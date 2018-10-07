package com.home.joseki.repositoryviewer.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.interfaces.ICommitAdapterListener;
import com.home.joseki.repositoryviewer.models.Commits;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommitsAdapter extends RecyclerView.Adapter<CommitsAdapter.CommitViewHolder>  {
    private List<Commits> commitList = new ArrayList<>();
    private ICommitAdapterListener commitAdapterListener = null;

    public void setListeners(ICommitAdapterListener listener){
        commitAdapterListener = listener;
    }

    public void setItems(Collection<Commits> list){
        commitList.addAll(list);
        notifyDataSetChanged();
    }

    class CommitViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCommitDate;
        private TextView tvCommitName;
        private TextView tvUserName;
        private ImageView ivUserIcon;

        CommitViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserIcon = itemView.findViewById(R.id.iv_lcommit_user_icon);
            tvCommitDate = itemView.findViewById(R.id.tv_lcommit_date);
            tvCommitName = itemView.findViewById(R.id.tv_lcommit_name);
            tvUserName = itemView.findViewById(R.id.tv_lcommit_user);
        }

        void bind(Commits commits){
            if(commits != null && commits.getAuthor() != null){
                Picasso.get().load(commits.getAuthor().getAvatar_url()).into(ivUserIcon);
                tvUserName.setText(commits.getAuthor().getLogin());
            } else {
                tvUserName.setText("-");
            }

            if(commits != null && commits.getCommit() != null && commits.getCommit().getCommitter() != null){
                tvCommitDate.setText(commits.getCommit().getCommitter().getDate());
                tvCommitName.setText(commits.getCommit().getMessage());
            } else {
                tvCommitDate.setText("-");
                tvCommitName.setText("-");
            }
        }
    }

    @NonNull
    @Override
    public CommitsAdapter.CommitViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listelement_commit, viewGroup, false);
        return new CommitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitsAdapter.CommitViewHolder contributorsViewHolder, int i) {
        contributorsViewHolder.bind(commitList.get(i));

        if(i == commitList.size() - 1) {
            commitAdapterListener.onBottomReached();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommitsAdapter.CommitViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return commitList.size();
    }
}
