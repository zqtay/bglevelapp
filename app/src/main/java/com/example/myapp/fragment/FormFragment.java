package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

        Button dateButton = (Button) view.findViewById(R.id.button_date);
        Button addButton = (Button) view.findViewById(R.id.button_add);

        // Date button
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(Util.DATE_PATTERN);
        String formattedDate = df.format(c.getTime());

        if (formattedDate != null) {
            dateButton.setText(formattedDate);
        }

        // Setup onclick events
        dateButton.setOnClickListener(this::showDatePickerDialog);
        addButton.setOnClickListener(this::onClickAdd);
        addButton.setOnLongClickListener(this::onLongClickClear);
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
        String bglevel_pre = ((EditText) activity.findViewById(R.id.textInput_bglevel_pre)).getText().toString();
        String bglevel_post = ((EditText) activity.findViewById(R.id.textInput_bglevel_post)).getText().toString();
        String dose = ((EditText) activity.findViewById(R.id.textInput_dose)).getText().toString();
        String notes = ((EditText) activity.findViewById(R.id.textInput_notes)).getText().toString();

        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event1);
        Float dataBglevel_pre = (bglevel_pre != null && !bglevel_pre.trim().equals("")) ? Float.parseFloat(bglevel_pre) : null;
        Float dataBglevel_post = (bglevel_post != null && !bglevel_post.trim().equals("")) ? Float.parseFloat(bglevel_post) : null;
        Float dataDose = (dose != null && !dose.trim().equals("")) ? Float.parseFloat(dose) : null;
        String dataNotes = (notes != null && !notes.trim().equals("")) ? notes : null;

        BGRecord record = new BGRecord(dataDate, dataEvent, dataBglevel_pre, dataBglevel_post, dataDose, dataNotes);
        AppDatabaseService.addUpdateRecord(record, activity.getApplicationContext());

        // Hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("error", "setUserVisibleHint: ", e);
        }
    }

    public boolean onLongClickClear(View v) {
        Activity activity = getActivity();
        ((EditText) activity.findViewById(R.id.textInput_bglevel_pre)).setText("");
        ((EditText) activity.findViewById(R.id.textInput_bglevel_post)).setText("");
        ((EditText) activity.findViewById(R.id.textInput_dose)).setText("");
        ((EditText) activity.findViewById(R.id.textInput_notes)).setText("");

        // Hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("error", "setUserVisibleHint: ", e);
        }
        return true;
    }
}
