package com.example.myapp.db;

import androidx.room.Database;
import androidx.room.DatabaseView;
import androidx.room.RoomDatabase;

@Database(entities = {BGRecord.class}, views={}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BGRecordDao bgRecordDao();
}

