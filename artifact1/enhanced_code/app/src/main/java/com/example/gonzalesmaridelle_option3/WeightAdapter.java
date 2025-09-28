package com.example.gonzalesmaridelle_option3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private List<WeightEntry> weightList;
    private OnDeleteClickListener onDeleteClickListener; // Interface for delete action

    // Constructor to initialize the list of WeightEntry objects and the delete listener
    public WeightAdapter(List<WeightEntry> weightList, OnDeleteClickListener onDeleteClickListener) {
        this.weightList = weightList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public WeightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_item, parent, false);
        return new WeightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeightViewHolder holder, int position) {
        WeightEntry weightEntry = weightList.get(position);
        holder.dateTextView.setText(weightEntry.getDate());
        holder.weightTextView.setText(weightEntry.getWeight() + " lb");

        // Handle the delete button click
        holder.deleteButton.setOnClickListener(v -> {
            // Call the onDeleteClickListener to notify the activity
            onDeleteClickListener.onDeleteClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    // ViewHolder class to hold individual item views
    public static class WeightViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView weightTextView;
        public Button deleteButton; // Delete button

        public WeightViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            weightTextView = itemView.findViewById(R.id.textViewWeight);
            deleteButton = itemView.findViewById(R.id.buttonDelete); // Bind the delete button
        }
    }

    // Method to update the list of weight entries
    public void updateWeightList(List<WeightEntry> newWeightList) {
        weightList = newWeightList;
        notifyDataSetChanged();
    }
}

