package com.example.myapp.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.EmptyResultSetException;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapp.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppDatabaseService {

    private static AppDatabase db;
    private static BGRecordDao dao;

    public static void buildDatabase(Context context){
        db = Room.databaseBuilder(context,
                AppDatabase.class, "bgrecord-database").build();
        dao = db.bgRecordDao();
    }

    public static BGRecord findRecord(int date, byte event, Context context) {
        BGRecord record = null;
        if (dao == null) {
            buildDatabase(context);
        }
        try {
            record = dao.find(date, event)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable().blockingFirst(null);
        }
        catch (EmptyResultSetException ex) {

        }
        return record;
    }

    public static List<BGRecord> findAllRecord(Context context) {
        List<BGRecord> records = null;
        if (dao == null) {
            buildDatabase(context);
        }
        try {
            records = dao.findAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable().blockingFirst(null);
        }
        catch (EmptyResultSetException ex) {

        }
        return records;
    }

    public static List<BGRecord> findRecordsByDate(int date, Context context) {
        List<BGRecord> records = null;
        if (dao == null) {
            buildDatabase(context);
        }
        try {
            records = dao.findByDate(date)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable().blockingFirst(null);
        }
        catch (EmptyResultSetException ex) {

        }
        return records;
    }

    public static void addRecord(BGRecord record, Context context) {
        if (dao == null) {
            buildDatabase(context);
        }
        dao.insert(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void updateRecord(BGRecord record, Context context) {
        if (dao == null) {
            buildDatabase(context);
        }
        dao.update(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void addUpdateRecord(BGRecord record, Context context) {
        AsyncTask.execute(() -> {
            BGRecord exRecord = findRecord(record.date, record.event, context);
            if (exRecord == null) {
                addRecord(record, context);
            }
            else {
                // Update new record empty fields with existing record
                // To prevent overwrite existing record with empty fields
                if (record.bglevel_pre == null){
                    record.bglevel_pre = exRecord.bglevel_pre;
                }
                if (record.bglevel_post == null){
                    record.bglevel_post = exRecord.bglevel_post;
                }
                if (record.dose == null){
                    record.dose = exRecord.dose;
                }
                if (record.notes == null){
                    record.notes = exRecord.notes;
                }
                updateRecord(record, context);
            }
        });
    }

    public static void deleteRecord(int date, byte event, Context context) {
        if (dao == null) {
            buildDatabase(context);
        }
        dao.delete(date, event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void insertAllRecords(List<BGRecord> records, Context context) {
        if (dao == null) {
            buildDatabase(context);
        }
        dao.insertAll(records)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Records saved", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void getView(Context context) {
        if (dao == null) {
            buildDatabase(context);
        }
    }

}
