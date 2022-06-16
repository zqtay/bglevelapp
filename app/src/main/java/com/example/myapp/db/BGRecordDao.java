package com.example.myapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface BGRecordDao {
    @Query("SELECT * FROM bgrecord WHERE date = :date AND " +
            "event = :event LIMIT 1")
    Single<BGRecord> find(int date, byte event);

    @Query("SELECT * FROM bgrecord")
    Flowable<List<BGRecord>> findAll();

    @Query("SELECT * FROM bgrecord WHERE date IN (:dates)")
    Flowable<List<BGRecord>> loadAllByDates(int[] dates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(BGRecord record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(BGRecord... records);

    @Update
    Completable update(BGRecord record);

    @Query("UPDATE bgrecord " +
            "SET bglevel_pre = :bglevel_pre " +
            "WHERE date = :date AND event = :event")
    Completable updateBGLevelPre(int date, byte event, Float bglevel_pre);

    @Query("UPDATE bgrecord " +
            "SET bglevel_pre = :bglevel_post " +
            "WHERE date = :date AND event = :event")
    Completable updateBGLevelPost(int date, byte event, Float bglevel_post);

    @Query("UPDATE bgrecord " +
            "SET dose = :dose " +
            "WHERE date = :date AND event = :event")
    Completable updateDose(int date, byte event, Float dose);

    @Query("UPDATE bgrecord " +
            "SET notes = :notes " +
            "WHERE date = :date AND event = :event")
    Completable updateNotes(int date, byte event, String notes);

    @Delete
    Completable delete(BGRecord record);

    @Query("DELETE from bgrecord WHERE date = :date AND event = :event")
    Completable delete(int date, byte event);

    //@Query("SELECT * FROM bgrecordview")
    //LiveData<List<BGRecordView>> getView();
}
