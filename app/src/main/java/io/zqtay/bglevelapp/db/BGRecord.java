package io.zqtay.bglevelapp.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"date","event"})
public class BGRecord {
    public static final byte EVENT1_WAKEUP = (byte)0x01;
    public static final byte EVENT1_BREAKFAST = (byte)0x02;
    public static final byte EVENT1_LUNCH = (byte)0x03;
    public static final byte EVENT1_SNACK = (byte)0x04;
    public static final byte EVENT1_DINNER = (byte)0x05;
    public static final byte EVENT1_SUPPER = (byte)0x06;
    public static final byte EVENT1_SLEEP = (byte)0x07;

    public static final byte EVENT2_PRE = (byte)0x10;
    public static final byte EVENT2_POST = (byte)0x20;


    public BGRecord(int date, byte event, Float bglevel_pre, Float bglevel_post, Float dose, String notes) {
        this.date = date;
        this.event = event;
        this.bglevel_pre = bglevel_pre;
        this.bglevel_post = bglevel_post;
        this.dose = dose;
        this.notes = notes;
    }

    @NonNull
    public int date;
    public byte event;

    @ColumnInfo(name = "bglevel_pre")
    @Nullable
    public Float bglevel_pre;

    @ColumnInfo(name = "bglevel_post")
    @Nullable
    public Float bglevel_post;

    @ColumnInfo(name = "dose")
    @Nullable
    public Float dose;

    @ColumnInfo(name = "notes")
    @Nullable
    public String notes;
}