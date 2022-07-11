package com.example.myapp.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Calendar cal;
    private View viewToUpdate;

    DatePickerFragment() {
        cal = Calendar.getInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
    }

    public void setDate(int date) {
        cal.set(Calendar.YEAR, Math.floorDiv(date, 10000));
        cal.set(Calendar.MONTH, (int) Math.floor(((date % 10000) / 100)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, (int) (date % 100));
    }

    // This will both update the date set when in dialog.show()
    // and also the date value of the calToSet in onDateSet()
    public void setCal(Calendar calToSet) {
        cal = calToSet;
    }

    public void setViewToUpdate(View viewToUpdate) {
        this.viewToUpdate = viewToUpdate;
    }

    public Calendar getCal() {
        return this.cal;
    }

    public View getViewToUpdate() {
        return this.viewToUpdate;
    }
}