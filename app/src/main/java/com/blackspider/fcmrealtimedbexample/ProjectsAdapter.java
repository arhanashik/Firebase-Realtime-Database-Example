package com.blackspider.fcmrealtimedbexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>{
    private List<Project> projects;
    private OnProjectSelectedListener listener;

    public ProjectsAdapter(List<Project> projects){
        this.projects = projects;
    }

    public void setProjectSelectedListener(OnProjectSelectedListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);

        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.bind(projects.get(position));
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name, platform;

        public ProjectViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_project_name);
            platform = itemView.findViewById(R.id.txt_platform);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onProjectSelected(projects.get(getAdapterPosition()));
        }

        void bind(Project project){
            name.setText("Title: " + project.getProjectName());
            platform.setText("Platform: " + project.getPlatform());
        }
    }

    public interface OnProjectSelectedListener {
        void onProjectSelected(Project project);
    }
}
