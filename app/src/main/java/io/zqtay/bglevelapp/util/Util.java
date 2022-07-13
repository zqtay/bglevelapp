package io.zqtay.bglevelapp.util;

import io.zqtay.bglevelapp.db.BGRecord;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Util {

    public final static int RESULT_SUCCESS = 0;
    public final static int RESULT_FAILED = -1;

    public final static String DATE_PATTERN = "yyyy/MM/dd";
    public final static String DATE_PATTERN_DATA = "yyyyMMdd";

    public static final int ACTIVITY_REQUEST_EXPORT_FILE_BACKUP = 0x9001;
    public static final int ACTIVITY_REQUEST_EXPORT_FILE_EXTENDED = 0x9002;
    public static final int ACTIVITY_REQUEST_IMPORT_FILE = 0x9003;
    public static final byte EXPORT_FORMAT_BACKUP = 0x01;
    public static final byte EXPORT_FORMAT_EXTENDED = 0x02;

    public static final String BGRECORD_EXPORT_FILENAME_BACKUP = "BGRecordBackup.txt";
    public static final String BGRECORD_EXPORT_FILENAME_EXTENDED = "BGRecordExt.txt";

    public static final String BGRECORD_HEADER = "date,event,bglevel_pre,bglevel_post,dose,notes\n";
    public static final String BGRECORD_HEADER_EXTENDED =
            "date," +
            "wakeup_bglevel_pre,wakeup_bglevel_post,wakeup_dose,wakeup_notes," +
            "breakfast_bglevel_pre,breakfast_bglevel_post,breakfast_dose,breakfast_notes," +
            "lunch_bglevel_pre,lunch_bglevel_post,lunch_dose,lunch_notes," +
            "snack_bglevel_pre,snack_bglevel_post,snack_dose,snack_notes," +
            "dinner_bglevel_pre,dinner_bglevel_post,dinner_dose,dinner_notes," +
            "supper_bglevel_pre,supper_bglevel_post,supper_dose,supper_notes," +
            "sleep_bglevel_pre,sleep_bglevel_post,sleep_dose,sleep_notes\n";

    public static byte convertEvent(String event) {
        byte result = (byte)0;
        if (event == null) {
            return result;
        }

        if (event.equals("Wake-up")) {
            result |= BGRecord.EVENT1_WAKEUP;
        }
        else if (event.equals("Breakfast")) {
            result |= BGRecord.EVENT1_BREAKFAST;
        }
        else if (event.equals("Lunch")) {
            result |= BGRecord.EVENT1_LUNCH;
        }
        else if (event.equals("Snack")) {
            result |= BGRecord.EVENT1_SNACK;
        }
        else if (event.equals("Dinner")) {
            result |= BGRecord.EVENT1_DINNER;
        }
        else if (event.equals("Supper")) {
            result |= BGRecord.EVENT1_SUPPER;
        }
        else if (event.equals("Sleep")) {
            result |= BGRecord.EVENT1_SLEEP;
        }

        return result;
    }

    public static String convertEvent(int event) {
        String result = "";

        switch (event) {
            case BGRecord.EVENT1_WAKEUP:
                result = "Wake-up";
                break;
            case BGRecord.EVENT1_BREAKFAST:
                result = "Breakfast";
                break;
            case BGRecord.EVENT1_LUNCH:
                result = "Lunch";
                break;
            case BGRecord.EVENT1_SNACK:
                result = "Snack";
                break;
            case BGRecord.EVENT1_DINNER:
                result = "Dinner";
                break;
            case BGRecord.EVENT1_SUPPER:
                result = "Supper";
                break;
            case BGRecord.EVENT1_SLEEP:
                result = "Sleep";
                break;
            default:
                result = "";
                break;
        }

        return result;
    }

    public static int convertDate(String date) {
        int iDate = (int)0;
        String[] saDate = null;
        if (date == null) {
            return iDate;
        }

        date = date.trim();

        // String "2022/03/04" (04 MAR 2022) = int (decimal) 20220304
        if (date.contains("/")) {
            saDate =  date.split("/");
        }
        else if (date.contains("-")){
            saDate =  date.split("-");
        }
        else if (date.length() == 8) {
            saDate = new String[3];
            saDate[0] = date.substring(0, 4);
            saDate[1] = date.substring(4, 6);
            saDate[2] = date.substring(6, 8);
        }

        if (saDate.length == 3) {
            iDate += Integer.parseInt(saDate[0]) * 10000;
            iDate += Integer.parseInt(saDate[1]) * 100;
            iDate += Integer.parseInt(saDate[2]);
        }

        return iDate;
    }

    public static String formatDateString(String date) {
        if (date != null && !date.isEmpty()) {
            date = date.trim();
            return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
        }
        else {
            return "";
        }
    }

    public static String formatDateString(Calendar cal, String format) {
        return (new SimpleDateFormat(format)).format(cal.getTime());
    }

    public static String formatDateString(Calendar cal) {
        return formatDateString(cal, Util.DATE_PATTERN);
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
        BGRecord record;

        if (records == null) {
            return recordsText;
        }

        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            recordLine = "";
            recordLine += String.valueOf(record.date);
            recordLine += ",";
            recordLine += String.valueOf(record.event);
            recordLine += ",";
            recordLine += (record.bglevel_pre != null) ? String.format("%.01f", record.bglevel_pre) : "";
            recordLine += ",";
            recordLine += (record.bglevel_post != null) ? String.format("%.01f", record.bglevel_post) : "";
            recordLine += ",";
            recordLine += (record.dose != null) ? String.format("%.01f", record.dose) : "";
            recordLine += ",";
            recordLine += (record.notes != null) ? "\""+record.notes+"\"" : "";
            recordLine += "\n";
            recordsText += recordLine;
        }

        return recordsText;
    }

    public static String getRecordsTextExtended(List<BGRecord> records) {
        String recordsText = "";
        String recordLine = "";
        BGRecord record;
        int recordsSize = records.size();
        byte lastEvent = 0;

        if (records == null) {
            return recordsText;
        }

        for (int i = 0; i < recordsSize; i++) {
            record = records.get(i);

            // Start of next date record
            if (lastEvent == 0) {
                recordLine += String.valueOf(record.date);
                recordLine += ",";
            }

            // Skip empty event data
            for (int j = 0; j < (record.event-lastEvent-1); j++) {
                recordLine += ",,,,";
            }

            recordLine += (record.bglevel_pre != null) ? String.format("%.01f", record.bglevel_pre) : "";
            recordLine += ",";
            recordLine += (record.bglevel_post != null) ? String.format("%.01f", record.bglevel_post) : "";
            recordLine += ",";
            recordLine += (record.dose != null) ? String.format("%.01f", record.dose) : "";
            recordLine += ",";
            recordLine += (record.notes != null) ? "\""+record.notes+"\"" : "";

            // Final record or next record is different date
            if (i == (recordsSize-1) || record.date != records.get(i+1).date) {
                recordLine += ("\n");
                recordsText += recordLine;
                lastEvent = 0;
                recordLine = "";
            }
            else {
                recordLine += ",";
                lastEvent = record.event;
            }
        }

        return recordsText;
    }

    public static List<BGRecord> getRecordsFromText(String recordsText) {
        if (recordsText == null) {
            return null;
        }
        List<BGRecord> records = new ArrayList<BGRecord>();
        String[] saRecords = recordsText.trim().split("\n");
        String[] saRecordInfo;
        int date;
        byte event;
        Float bglevel_pre;
        Float bglevel_post;
        Float dose;
        String notes;

        // Skip header
        int iLine = saRecords[0].trim().equals(BGRECORD_HEADER.trim()) ? 1 : 0;

        for (; iLine < saRecords.length; iLine++) {
            try {
                saRecordInfo = saRecords[iLine].split(",", -1);
                date = Integer.parseInt(saRecordInfo[0]);
                event = Byte.parseByte(saRecordInfo[1]);
                bglevel_pre = !saRecordInfo[2].isEmpty() ? Float.parseFloat(saRecordInfo[2]) : null;
                bglevel_post = !saRecordInfo[3].isEmpty() ? Float.parseFloat(saRecordInfo[3]) : null;
                dose = !saRecordInfo[4].isEmpty() ? Float.parseFloat(saRecordInfo[4]) : null;
                notes = saRecordInfo[5];
                // Remove starting and ending quotes
                if (notes.startsWith("\"") && notes.endsWith("\"")) {
                    notes = notes.substring(1, notes.length() - 1);
                }
                records.add(new BGRecord(date, event, bglevel_pre, bglevel_post, dose, notes));
            }
            catch (Exception e) {
                Log.e("Exception", String.format("%s\nParse records text failed at line %d: %s",
                        e.toString(), iLine+1, saRecords[iLine]));
                return null;
            }
        }
        return records;
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

    public static String readFromFile(Uri fileUri, Context context) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            InputStreamReader isr = new InputStreamReader(context.getContentResolver().openInputStream(fileUri));
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
            return null;
        }
        return text.toString();
    }
}
