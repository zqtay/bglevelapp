package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.EmptyResultSetException;

import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    final static String datePattern = "yyyy/MM/dd";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Date button
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(datePattern);
        String formattedDate = df.format(c.getTime());

        if (formattedDate != null) {
            ((Button) findViewById(R.id.button_date)).setText(formattedDate);
            // ((Button) findViewById(R.id.button_time)).setText(formattedDate[1]);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public static View viewToUpdate;

        DatePickerFragment(View view) {
            viewToUpdate = view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (viewToUpdate instanceof Button) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                String date = (new SimpleDateFormat(datePattern)).format(c.getTime());
                ((Button)viewToUpdate).setText(date);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClickAdd(View v) {
        String date = ((Button) findViewById(R.id.button_date)).getText().toString();
        String event1 = ((Spinner) findViewById(R.id.spinner_event1)).getSelectedItem().toString();
        String event2 = ((Spinner) findViewById(R.id.spinner_event2)).getSelectedItem().toString();
        String bglevel = ((EditText) findViewById(R.id.textInput_bglevel)).getText().toString();
        String dose = ((EditText) findViewById(R.id.textInput_dose)).getText().toString();
        String notes = ((EditText) findViewById(R.id.textInput_notes)).getText().toString();

        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event1);
        Float dataBglevel = (bglevel != null && !bglevel.trim().equals("")) ? Float.parseFloat(bglevel) : null;
        Float dataDose = (dose != null && !dose.trim().equals("")) ? Float.parseFloat(dose) : null;
        String dataNotes = (notes != null && !notes.trim().equals("")) ? notes : null;
        Float bglevel_pre = (event2.equals("Pre")) ? dataBglevel : null;
        Float bglevel_post = (event2.equals("Post")) ? dataBglevel : null;

        BGRecord record = new BGRecord(dataDate, dataEvent, bglevel_pre, bglevel_post, dataDose, dataNotes);
        AppDatabaseService.addUpdateRecord(record, getApplicationContext());
    }

    public void onClickSearch(View v) {
        Context context = getApplicationContext();
        String date = ((Button) findViewById(R.id.button_date)).getText().toString();
        String event1 = ((Spinner) findViewById(R.id.spinner_event1)).getSelectedItem().toString();
        String event2 = ((Spinner) findViewById(R.id.spinner_event2)).getSelectedItem().toString();

        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event1);

        AsyncTask.execute(()-> {
            BGRecord record = AppDatabaseService.findRecord(dataDate, dataEvent, context);
            Util.printRecord(record);

            runOnUiThread(()-> {
                if (record != null) {
                    if (event2.equals("Pre")) {
                        ((EditText) findViewById(R.id.textInput_bglevel)).setText((record.bglevel_pre != null) ? String.format("%.01f", record.bglevel_pre) : "");
                    } else if (event2.equals("Post")) {
                        ((EditText) findViewById(R.id.textInput_bglevel)).setText((record.bglevel_post != null) ? String.format("%.01f", record.bglevel_post) : "");
                    }
                    ((EditText) findViewById(R.id.textInput_dose)).setText((record.bglevel_pre != null) ? String.format("%.01f", record.dose) : "");
                    ((EditText) findViewById(R.id.textInput_notes)).setText(record.notes);
                }
                else {
                    Toast.makeText(context, "No matching record", Toast.LENGTH_SHORT).show();
                    ((EditText) findViewById(R.id.textInput_bglevel)).setText("");
                    ((EditText) findViewById(R.id.textInput_dose)).setText("");
                    ((EditText) findViewById(R.id.textInput_notes)).setText("");
                }
            });
        });
    }

    public void onClickDelete(View v) {
        String date = ((Button) findViewById(R.id.button_date)).getText().toString();
        String event1 = ((Spinner) findViewById(R.id.spinner_event1)).getSelectedItem().toString();

        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event1);

        AppDatabaseService.deleteRecord(dataDate, dataEvent, getApplicationContext());
    }


    // Not used
    // public static class TimePickerFragment extends DialogFragment
    //         implements TimePickerDialog.OnTimeSetListener {
    //
    //     public static View viewToUpdate;
    //
    //     TimePickerFragment(View view) {
    //         viewToUpdate = view;
    //     }
    //
    //     @Override
    //     public Dialog onCreateDialog(Bundle savedInstanceState) {
    //         // Use the current time as the default values for the picker
    //         final Calendar c = Calendar.getInstance();
    //         int hour = c.get(Calendar.HOUR_OF_DAY);
    //         int minute = c.get(Calendar.MINUTE);
    //
    //         // Create a new instance of TimePickerDialog and return it
    //         return new TimePickerDialog(getActivity(), this, hour, minute,
    //                 DateFormat.is24HourFormat(getActivity()));
    //     }
    //
    //     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    //         if (viewToUpdate instanceof Button) {
    //             ((Button)viewToUpdate).setText("abc");
    //         }
    //     }
    // }
    //
    // public void showTimePickerDialog(View v) {
    //     DialogFragment newFragment = new TimePickerFragment(v);
    //     newFragment.show(getSupportFragmentManager(), "timePicker");
    // }
}

