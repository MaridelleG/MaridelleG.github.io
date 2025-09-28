package com.example.gonzalesmaridelle_option3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddWeightActivity extends AppCompatActivity {

    private EditText editTextDate, editTextWeight;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_weight);

        editTextDate = findViewById(R.id.editTextText3);
        editTextWeight = findViewById(R.id.editTextText4);
        buttonSubmit = findViewById(R.id.button3);

        buttonSubmit.setOnClickListener(v -> {
            String date = editTextDate.getText().toString().trim();
            String weight = editTextWeight.getText().toString().trim();

            if (date.isEmpty() || weight.isEmpty()) {
                Toast.makeText(AddWeightActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Send the new weight data back to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("date", date);
                resultIntent.putExtra("weight", weight);
                setResult(RESULT_OK, resultIntent);
                finish(); // Finish the activity and return to MainActivity
            }
        });
    }
}
