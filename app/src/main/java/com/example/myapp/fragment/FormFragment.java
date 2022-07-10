package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    Activity activity;
    DatePickerFragment datePicker;
    String formDate;
    String formEvent;
    String formBgLevelPre;
    String formBgLevelPost;
    String formDose;
    String formNotes;

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

        activity = getActivity();

        Button dateButton = (Button) view.findViewById(R.id.form_button_date);
        Button addButton = (Button) view.findViewById(R.id.form_button_add);
        datePicker = new DatePickerFragment(dateButton);

        // Date button
        Calendar c = Calendar.getInstance();
        Log.d("debug","Current time => "+c.getTime());
        String formattedDate = (new SimpleDateFormat(Util.DATE_PATTERN)).format(c.getTime());
        dateButton.setText(formattedDate);

        // Setup onclick events
        dateButton.setOnClickListener(this::showDatePickerDialog);
        addButton.setOnClickListener(this::onClickAdd);
        addButton.setOnLongClickListener(this::onLongClickClear);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        View viewToUpdate;
        Calendar lastSetCal;

        DatePickerFragment(View view) {
            viewToUpdate = view;
            lastSetCal = Calendar.getInstance();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = lastSetCal.get(Calendar.YEAR);
            int month = lastSetCal.get(Calendar.MONTH);
            int day = lastSetCal.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            lastSetCal.set(Calendar.YEAR, year);
            lastSetCal.set(Calendar.MONTH, month);
            lastSetCal.set(Calendar.DAY_OF_MONTH, day);
            if (viewToUpdate instanceof TextView) {
                String date = (new SimpleDateFormat(Util.DATE_PATTERN)).format(lastSetCal.getTime());
                ((TextView)viewToUpdate).setText(date);
            }
        }

        public void setDate(String date) {
            // date is formatted as yyyy/MM/dd
            lastSetCal.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
            lastSetCal.set(Calendar.MONTH, Integer.parseInt(date.substring(5,7)));
            lastSetCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8,10)));
        }
    }

    public void showDatePickerDialog(View v) {
        if (datePicker == null || !(datePicker instanceof DatePickerFragment)) {
            datePicker = new DatePickerFragment(v);
        }
        datePicker.show(((FragmentActivity)activity).getSupportFragmentManager(), "datePickerForm");
    }

    public void onClickAdd(View v) {
        String date = ((Button) activity.findViewById(R.id.form_button_date)).getText().toString();
        String event1 = ((Spinner) activity.findViewById(R.id.form_spinner_event)).getSelectedItem().toString();
        String bglevel_pre = ((EditText) activity.findViewById(R.id.form_input_bgLevelPre)).getText().toString();
        String bglevel_post = ((EditText) activity.findViewById(R.id.form_input_bgLevelPost)).getText().toString();
        String dose = ((EditText) activity.findViewById(R.id.form_input_dose)).getText().toString();
        String notes = ((EditText) activity.findViewById(R.id.form_input_notes)).getText().toString();

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
        ((EditText) activity.findViewById(R.id.form_input_bgLevelPre)).setText("");
        ((EditText) activity.findViewById(R.id.form_input_bgLevelPost)).setText("");
        ((EditText) activity.findViewById(R.id.form_input_dose)).setText("");
        ((EditText) activity.findViewById(R.id.form_input_notes)).setText("");

        // Hide keyboard
        try {
            View focusedView = null;
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            if ((focusedView = activity.getCurrentFocus()) != null) {
                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("error", "setUserVisibleHint: ", e);
        }
        return true;
    }

    public void setFormValues(String date, String event, String bglevel_pre, String bglevel_post, String dose, String notes) {
        formDate = date;
        formEvent = event;
        formBgLevelPre = bglevel_pre;
        formBgLevelPost = bglevel_post;
        formDose = dose;
        formNotes = notes;
    }

    public void displayFormValues() {
        if (formDate != null && !formDate.isEmpty()) {
            ((Button) activity.findViewById(R.id.form_button_date)).setText(formDate);
            datePicker.setDate(formDate);
        }
        if (formEvent != null)
            ((Spinner) activity.findViewById(R.id.form_spinner_event)).setSelection(Util.convertEvent(formEvent) - 1);
        if (formBgLevelPre != null)
            ((EditText) activity.findViewById(R.id.form_input_bgLevelPre)).setText(formBgLevelPre);
        if (formBgLevelPost != null)
            ((EditText) activity.findViewById(R.id.form_input_bgLevelPost)).setText(formBgLevelPost);
        if (formDose != null)
            ((EditText) activity.findViewById(R.id.form_input_dose)).setText(formDose);
        if (formNotes != null)
            ((EditText) activity.findViewById(R.id.form_input_notes)).setText(formNotes);

        formDate = null;
        formEvent = null;
        formBgLevelPre = null;
        formBgLevelPost = null;
        formDose = null;
        formNotes = null;
    }
}
