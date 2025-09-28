package com.example.gonzalesmaridelle_option3;

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

        // Initialize the buttons and RecyclerView
        buttonAddWeight = findViewById(R.id.button3);
        buttonSMSPermission = findViewById(R.id.buttonSMSPermission);
        weightListRecyclerView = findViewById(R.id.weightListRecyclerView);

        // Initialize the weight list
        weightList = new ArrayList<>();
        weightAdapter = new WeightAdapter(weightList, this);
        weightListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightListRecyclerView.setAdapter(weightAdapter);

        // Launch AddWeightActivity
        buttonAddWeight.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddWeightActivity.class);
            startActivityForResult(intent, ADD_WEIGHT_REQUEST);
        });

        // Launch SMSPermissionActivity
        buttonSMSPermission.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SMSPermissionActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WEIGHT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                String date = data.getStringExtra("date");
                String weight = data.getStringExtra("weight");

                if (date != null && weight != null) {
                    weightList.add(new WeightEntry(date, weight));
                    weightAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "New Entry: " + date + " - " + weight, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error: Invalid data received", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: No data received from AddWeightActivity", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Implement the delete functionality from the OnDeleteClickListener
    @Override
    public void onDeleteClick(int position) {
        // Remove the item from the list and update the RecyclerView
        weightList.remove(position);
        weightAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
    }
}