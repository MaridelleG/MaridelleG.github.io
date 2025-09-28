package com.example.gonzalesmaridelle_option3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "weight_entries")
public class WeightEntry {

    @PrimaryKey(autoGenerate = true)
    public int id; // Unique ID for each entry

    public String date; // Date of the entry (YYYY-MM-DD)
    public double weight; // Weight value in pounds

    // Constructor
    public WeightEntry(String date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public String getDate() { return date; }

    public String getWeight() {
        return String.valueOf(weight); // return string for display in adapter
    }

    public double getWeightValue() {
        return weight; // numeric value if needed for calculations
    }
}
