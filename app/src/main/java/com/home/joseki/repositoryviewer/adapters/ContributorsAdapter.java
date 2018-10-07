package com.home.joseki.repositoryviewer.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.models.Contributors;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorsViewHolder> {
    private List<Contributors> contributorList = new ArrayList<>();

    public void setItems(List<Contributors> list){
        contributorList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems(){
        contributorList.clear();
        notifyDataSetChanged();
    }

    class ContributorsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContributeCount;
        private TextView tvDeletions;
        private TextView tcAdditions;
        private TextView tvUserName;
        private ImageView ivUserIcon;

        ContributorsViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserIcon = (ImageView) itemView.findViewById(R.id.iv_lc_user_pic);
            tvContributeCount = (TextView) itemView.findViewById(R.id.tv_lc_contribute_count);
            tvDeletions = (TextView) itemView.findViewById(R.id.tv_lc_deletions);
            tcAdditions = (TextView) itemView.findViewById(R.id.tv_lc_additions);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_lc_user_name);
        }

        void bind(Contributors contributors){
            if(contributors == null || contributors.getAuthor() == null){
                return;
            }

            if(contributors.getWeeks() != null){
                int additions = 0;
                int deletions = 0;

                for(int i = 0; i< contributors.getWeeks().size() - 1; i++){
                    additions += Integer.parseInt(contributors.getWeeks().get(i).getA());
                    deletions += Integer.parseInt(contributors.getWeeks().get(i).getD());
                }

                tvDeletions.setText(String.valueOf(deletions));
                tcAdditions.setText(String.valueOf(additions));
            } else {
                tvDeletions.setText("-");
                tcAdditions.setText("-");
            }

            if(contributors.getAuthor()!= null){
                Picasso.get().load(contributors.getAuthor().getAvatar_url()).into(ivUserIcon);
            }

            if(contributors.getAuthor() != null){
                tvUserName.setText(contributors.getAuthor().getLogin());
            } else {
                tvUserName.setText("-");
            }

            tvContributeCount.setText(contributors.getTotal());
        }
    }

    @NonNull
    @Override
    public ContributorsAdapter.ContributorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listelement_contribute, viewGroup, false);
        return new ContributorsAdapter.ContributorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorsAdapter.ContributorsViewHolder contributorsViewHolder, int i) {
        contributorsViewHolder.bind(contributorList.get(i));
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorsAdapter.ContributorsViewHolder holder, int position, @NonNull List<Object> payloads) {
        final Contributors element = contributorList.get(position);

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }
}
