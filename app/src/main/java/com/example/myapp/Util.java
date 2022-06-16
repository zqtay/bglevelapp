package com.example.myapp;

import com.example.myapp.db.BGRecord;

import android.util.Log;

import com.example.myapp.db.BGRecord;

public class Util {

    public static byte convertEvent(String event1) {
        byte event = (byte)0;
        if (event1 == null) {
            return event;
        }

        if (event1.equals("Wake-up")) {
            event |= BGRecord.EVENT1_WAKEUP;
        }
        else if (event1.equals("Breakfast")) {
            event |= BGRecord.EVENT1_BREAKFAST;
        }
        else if (event1.equals("Lunch")) {
            event |= BGRecord.EVENT1_LUNCH;
        }
        else if (event1.equals("Snack")) {
            event |= BGRecord.EVENT1_SNACK;
        }
        else if (event1.equals("Dinner")) {
            event |= BGRecord.EVENT1_DINNER;
        }
        else if (event1.equals("Supper")) {
            event |= BGRecord.EVENT1_SUPPER;
        }
        else if (event1.equals("Sleep")) {
            event |= BGRecord.EVENT1_SLEEP;
        }

        return event;
    }

    public static int convertDate(String date) {
        int iDate = (int)0;
        String[] saDate = null;
        if (date == null) {
            return iDate;
        }

        // String "2022/03/04" (04 MAR 2022) = int (decimal) 20220304
        if (date.contains("/")) {
            saDate =  date.split("/");
        }
        else if (date.contains("-")){
            saDate =  date.split("-");
        }

        if (saDate.length == 3) {
            iDate += Integer.parseInt(saDate[0]) * 10000;
            iDate += Integer.parseInt(saDate[1]) * 100;
            iDate += Integer.parseInt(saDate[2]);
        }

        return iDate;
    }

    public static void printRecord(BGRecord record) {
        if (record == null) {
            Log.d("debug","Null record");
            return;
        }
        Log.d("debug","Date: " + record.date);
        Log.d("debug","Event: " + record.event);
        Log.d("debug","BG_Pre: " + record.bglevel_pre);
        Log.d("debug","BG_Post: " + record.bglevel_post);
        Log.d("debug","Dose: " + record.dose);
        Log.d("debug","Notes: " + record.notes);
    }
}
