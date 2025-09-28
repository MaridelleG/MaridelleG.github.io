package com.example.gonzalesmaridelle_option3;
import com.example.gonzalesmaridelle_option3.AppDatabase;
import com.example.gonzalesmaridelle_option3.WeightEntry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WeightAdapter.OnDeleteClickListener {

    private Button buttonAddWeight, buttonSMSPermission;
    private RecyclerView weightListRecyclerView;
    private WeightAdapter weightAdapter;
    private ArrayList<WeightEntry> weightList;

    private static final int ADD_WEIGHT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons and RecyclerView
        buttonAddWeight = findViewById(R.id.buttonAddWeight);
        buttonSMSPermission = findViewById(R.id.buttonSmsPermission);
        weightListRecyclerView = findViewById(R.id.recyclerViewWeights);

        // Initialize weight list and adapter
        weightList = new ArrayList<>();
        weightAdapter = new WeightAdapter(weightList, this);
        weightListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightListRecyclerView.setAdapter(weightAdapter);

        // Load saved entries from database
        AppDatabase db = AppDatabase.getDatabase(this);
        new Thread(() -> {
            final ArrayList<WeightEntry> entries = new ArrayList<>(db.weightEntryDao().getAllEntries());
            runOnUiThread(() -> {
                weightList.addAll(entries);
                weightAdapter.notifyDataSetChanged();
            });
        }).start();

        // Add weight button
        buttonAddWeight.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddWeightActivity.class);
            startActivityForResult(intent, ADD_WEIGHT_REQUEST);
        });

        // SMS permission button
        buttonSMSPermission.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SMSPermissionActivity.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WEIGHT_REQUEST && resultCode == RESULT_OK && data != null) {
            String date = data.getStringExtra("date");
            double weight = data.getDoubleExtra("weight", -1);

            if (date != null && weight > 0) {
                // Create a new entry
                WeightEntry entry = new WeightEntry(date, weight);

                AppDatabase db = AppDatabase.getDatabase(this);

                new Thread(() -> {
                    db.weightEntryDao().insert(entry);
                    runOnUiThread(() -> {
                        weightList.add(entry);
                        weightAdapter.notifyItemInserted(weightList.size() - 1);
                        Toast.makeText(this, "New Entry: " + date + " - " + weight, Toast.LENGTH_SHORT).show();
                    });
                }).start();

                Toast.makeText(this, "New Entry: " + date + " - " + weight, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: Invalid data received", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Delete functionality from the adapter
    @Override
    public void onDeleteClick(int position) {
        WeightEntry entry = weightList.get(position);
        AppDatabase db = AppDatabase.getDatabase(this);

    // Delete in background
        new Thread(() -> {
            db.weightEntryDao().delete(entry);
            runOnUiThread(() -> {
                weightList.remove(position);
                weightAdapter.notifyItemRemoved(position);
                Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
            });
        }).start();

    }
}