package com.example.gonzalesmaridelle_option3;
import com.example.gonzalesmaridelle_option3.AppDatabase;
import com.example.gonzalesmaridelle_option3.WeightEntry;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

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

        // Disable manual typing for date, open DatePicker instead
        editTextDate.setFocusable(false);
        editTextDate.setOnClickListener(v -> showDatePicker());

        buttonSubmit.setOnClickListener(v -> {
            String date = editTextDate.getText().toString().trim();
            String weightStr = editTextWeight.getText().toString().trim();

            if (date.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(AddWeightActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                if (weight <= 0) {
                    Toast.makeText(this, "Please enter a valid positive weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Return the new entry to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("date", date);
                resultIntent.putExtra("weight", weight);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close activity

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Weight must be a number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format date as YYYY-MM-DD
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    editTextDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}