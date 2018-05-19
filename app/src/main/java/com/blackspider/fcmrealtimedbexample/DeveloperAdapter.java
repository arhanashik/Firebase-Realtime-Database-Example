package com.blackspider.fcmrealtimedbexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder>{
    private List<Developer> developers;
    private OnDeveloperSelectedListener listener;

    public DeveloperAdapter(List<Developer> developers){
        this.developers = developers;
    }

    public void setDeveloperSelectedListener(OnDeveloperSelectedListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeveloperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_developer, parent, false);

        return new DeveloperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeveloperViewHolder holder, int position) {
        holder.bind(developers.get(position));
    }

    @Override
    public int getItemCount() {
        return developers.size();
    }

    public class DeveloperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{
        private TextView name, expert;

        public DeveloperViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            expert = itemView.findViewById(R.id.txt_expert);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onDeveloperClicked(developers.get(getAdapterPosition()));
        }

        void bind(Developer developer){
            name.setText("Name: " + developer.getDeveloperName());
            expert.setText("Expert: " + developer.getDeveloperExpert());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onDeveloperLongClicked(developers.get(getAdapterPosition()));

            return true;
        }
    }

    public interface OnDeveloperSelectedListener{
        void onDeveloperClicked(Developer developer);
        void onDeveloperLongClicked(Developer developer);
    }
}
