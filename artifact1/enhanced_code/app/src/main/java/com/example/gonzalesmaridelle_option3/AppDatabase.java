package com.example.gonzalesmaridelle_option3;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database class.
 * Provides access to the database and DAO objects.
 */
@Database(entities = {WeightEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WeightEntryDao weightEntryDao(); // DAO access

    private static AppDatabase INSTANCE;

    /**
     * Singleton pattern to get database instance.
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
