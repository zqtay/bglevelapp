package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapp.R;
import com.example.myapp.util.Util;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Date button
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(Util.DATE_PATTERN);
        String formattedDate = df.format(c.getTime());

        if (formattedDate != null) {
            ((Button) view.findViewById(R.id.button_date)).setText(formattedDate);
        }

        // Setup onclick events
        view.findViewById(R.id.button_date).setOnClickListener(this::showDatePickerDialog);
        view.findViewById(R.id.button_add).setOnClickListener(this::onClickAdd);
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
                String date = (new SimpleDateFormat(Util.DATE_PATTERN)).format(c.getTime());
                ((Button)viewToUpdate).setText(date);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        FragmentActivity activity = getActivity();
        DialogFragment newFragment = new DatePickerFragment(v);
        newFragment.show(activity.getSupportFragmentManager(), "datePickerForm");
    }

    public void onClickAdd(View v) {
        Activity activity = getActivity();
        String date = ((Button) activity.findViewById(R.id.button_date)).getText().toString();
        String event1 = ((Spinner) activity.findViewById(R.id.spinner_event1)).getSelectedItem().toString();
        String event2 = ((Spinner) activity.findViewById(R.id.spinner_event2)).getSelectedItem().toString();
        String bglevel = ((EditText) activity.findViewById(R.id.textInput_bglevel)).getText().toString();
        String dose = ((EditText) activity.findViewById(R.id.textInput_dose)).getText().toString();
        String notes = ((EditText) activity.findViewById(R.id.textInput_notes)).getText().toString();

        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event1);
        Float dataBglevel = (bglevel != null && !bglevel.trim().equals("")) ? Float.parseFloat(bglevel) : null;
        Float dataDose = (dose != null && !dose.trim().equals("")) ? Float.parseFloat(dose) : null;
        String dataNotes = (notes != null && !notes.trim().equals("")) ? notes : null;
        Float bglevel_pre = (event2.equals("Pre")) ? dataBglevel : null;
        Float bglevel_post = (event2.equals("Post")) ? dataBglevel : null;

        BGRecord record = new BGRecord(dataDate, dataEvent, bglevel_pre, bglevel_post, dataDose, dataNotes);
        AppDatabaseService.addUpdateRecord(record, activity.getApplicationContext());
    }
}
