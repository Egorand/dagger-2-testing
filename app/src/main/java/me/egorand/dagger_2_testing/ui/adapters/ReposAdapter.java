package me.egorand.dagger_2_testing.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.egorand.dagger_2_testing.R;
import me.egorand.dagger_2_testing.data.Repo;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;

    private List<Repo> repos;

    public ReposAdapter(LayoutInflater layoutInflater, List<Repo> repos) {
        this.layoutInflater = layoutInflater;
        this.repos = new ArrayList<>(repos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.row_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repo repo = repos.get(position);
        holder.name.setText(repo.name);
        holder.description.setText(repo.description);
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
