package com.home.joseki.repositoryviewer.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.joseki.repositoryviewer.R;
import com.home.joseki.repositoryviewer.interfaces.IProjectAdapterListener;
import com.home.joseki.repositoryviewer.models.GitResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>{
    private List<GitResult> projList = new ArrayList<>();
    private final PublishSubject<GitResult> onClickSubject = PublishSubject.create();
    private IProjectAdapterListener projectAdapterListener = null;

    public void setListeners(IProjectAdapterListener listener){
        projectAdapterListener = listener;
    }

    public void setItems(Collection<GitResult> list){
        projList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems(){
        projList.clear();
        notifyDataSetChanged();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProjName;
        private TextView tvStarCount;
        private TextView tvForkCount;

        ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProjName = itemView.findViewById(R.id.tvProjName);
            tvStarCount = itemView.findViewById(R.id.tvStarCount);
            tvForkCount = itemView.findViewById(R.id.tvForkCount);
        }

        void bind(GitResult project){
            tvProjName.setText(project.getName());
            tvStarCount.setText(project.getStargazers_count());
            tvForkCount.setText(project.getForks_count());
        }
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listelement_project, viewGroup, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {
        projectViewHolder.bind(projList.get(i));

        if(i == projList.size() - 1) {
            projectAdapterListener.onBottomReached();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position, @NonNull List<Object> payloads) {
        final GitResult element = projList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(element);
            }
        });

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return projList.size();
    }

    public void addSubscribe(Consumer<? super GitResult> s){
        onClickSubject.subscribe(s);
    }
}
