package com.example.gonzalesmaridelle_option3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ItemWeightEntryActivity extends AppCompatActivity implements WeightAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private WeightAdapter adapter;
    private ArrayList<WeightEntry> weightEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.weightListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        weightEntries = new ArrayList<>();
        weightEntries.add(new WeightEntry("01/23/2025", "128 lb"));
        weightEntries.add(new WeightEntry("02/10/2025", "130 lb"));

        adapter = new WeightAdapter(weightEntries, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDeleteClick(int position) {
        weightEntries.remove(position);
        adapter.notifyItemRemoved(position);
    }
}

