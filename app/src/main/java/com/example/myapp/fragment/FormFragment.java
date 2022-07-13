package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormFragment extends Fragment {

    Activity activity;
    DatePickerFragment datePicker;
    ArrayAdapter<String> notesArrAdapter;
    String formDate;
    String formEvent;
    String formBgLevelPre;
    String formBgLevelPost;
    String formDose;
    String formNotes;
    int notesArrSize;

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
        datePicker = new FormDatePickerFragment();
        notesArrSize = 5;
        notesArrAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line,  new ArrayList<String>());

        Button dateButton = (Button) view.findViewById(R.id.form_button_date);
        Button addButton = (Button) view.findViewById(R.id.form_button_add);
        AutoCompleteTextView notesTextInput = (AutoCompleteTextView) view.findViewById(R.id.form_input_notes);

        // Date button
        dateButton.setText(Util.formatDateString(datePicker.getCal()));
        dateButton.setOnClickListener(this::showDatePickerDialog);

        // Add button
        addButton.setOnClickListener(this::onClickAdd);
        addButton.setOnLongClickListener(this::onLongClickClear);

        // Set autofill
        notesArrAdapter.setNotifyOnChange(false);
        notesTextInput.setThreshold(0);
        notesTextInput.setAdapter(notesArrAdapter);
        notesTextInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                notesTextInput.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClickNotes(v);
                }
                return true;
            }
        } );
    }

    public void showDatePickerDialog(View v) {
        datePicker.setViewToUpdate(v);
        datePicker.show(((FragmentActivity)activity).getSupportFragmentManager(), FormDatePickerFragment.TAG_SET_DATE);
    }

    public boolean onClickNotes(View v) {
        AsyncTask.execute(()-> {
            List<String> notes = AppDatabaseService.findPastNotes(notesArrSize, activity.getApplicationContext());
            activity.runOnUiThread(() -> {
                notesArrAdapter.clear();
                notesArrAdapter.addAll(notes);
                if (!((AutoCompleteTextView)v).isPopupShowing()) {
                    ((AutoCompleteTextView)v).showDropDown();
                }
            });
        });
        return true;
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
            datePicker.setDate(Util.convertDate(formDate));
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

    public static class FormDatePickerFragment extends DatePickerFragment {
        public static final String TAG_SET_DATE = "datePickerForm";
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            super.onDateSet(view, year, month, day);
            View v = getViewToUpdate();
            if (v instanceof TextView) {
                ((TextView)v).setText(Util.formatDateString(getCal()));
            }
        }
    }
}
