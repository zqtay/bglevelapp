package com.example.myapp.util;

import com.example.myapp.db.BGRecord;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.myapp.db.BGRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.List;

public class Util {

    public final static int RESULT_SUCCESS = 0;
    public final static int RESULT_FAILED = -1;


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

    public static String getRecordsText(List<BGRecord> records) {
        String recordsText = "";
        String recordLine = "";

        if (records == null) {
            return recordsText;
        }

        for (int i = 0; i < records.size(); i++) {
            recordLine = "";
            recordLine += String.valueOf(records.get(i).date);
            recordLine += (",");
            recordLine += String.valueOf(records.get(i).event);
            recordLine += (",");
            recordLine += (records.get(i).bglevel_pre != null) ? String.format("%.01f", records.get(i).bglevel_pre) : null;
            recordLine += (",");
            recordLine += (records.get(i).bglevel_post != null) ? String.format("%.01f", records.get(i).bglevel_post) : null;
            recordLine += (",");
            recordLine += (records.get(i).dose != null) ? String.format("%.01f", records.get(i).dose) : null;
            recordLine += (",");
            recordLine += records.get(i).notes;
            recordLine += ("\n");
            recordsText += recordLine;
        }

        return recordsText;
    }

    public static int writeToFile(String data, String filePath, String fileName) {
        int result = RESULT_FAILED;
        File fileDir = new File(filePath);
        File file = new File(filePath, fileName);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File creation failed: " + e.toString());
            result = RESULT_FAILED;
        }
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
            result = RESULT_SUCCESS;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            result = RESULT_FAILED;
        }
        return result;
    }
}
