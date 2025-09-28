package com.example.gonzalesmaridelle_option3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * Data Access Object for WeightEntry.
 * Contains methods to insert, delete, and fetch weight entries.
 */
@Dao
public interface WeightEntryDao {

    @Insert
    void insert(WeightEntry entry); // Add new weight entry

    @Delete
    void delete(WeightEntry entry); // Delete a specific entry

    @Query("SELECT * FROM weight_entries ORDER BY date ASC")
    List<WeightEntry> getAllEntries(); // Fetch all entries ordered by date
}
