package io.zqtay.bglevelapp.db;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.room.EmptyResultSetException;
import androidx.room.Room;

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

    public static List<BGRecord> findRecordsByDateRange(int dateStart, int dateEnd, Context context) {
        List<BGRecord> records = null;
        try {
            records = dao.findByDateRange(dateStart, dateEnd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable().blockingFirst(null);
        }
        catch (EmptyResultSetException ex) {

        }
        return records;
    }

    public static List<String> findPastNotes(int limit, Context context) {
        List<String> notes = null;
        try {
            notes = dao.findPastNotes(limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable().blockingFirst(null);
        }
        catch (EmptyResultSetException ex) {

        }
        return notes;
    }

    public static void addRecord(BGRecord record, Context context) {
        dao.insert(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void updateRecord(BGRecord record, Context context) {
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
        dao.delete(date, event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }

    public static void insertAllRecords(List<BGRecord> records, Context context) {
        dao.insertAll(records)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(context, "Records saved", Toast.LENGTH_SHORT)
                            .show();
                }, Throwable::printStackTrace );
    }
}
