package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;
import com.example.myapp.util.Util;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    public static final byte FILTER_BY_DATE = (byte)0x01;
    public static final byte FILTER_BY_DATE_RANGE = (byte)0x02;
    public static final byte FILTER_ALL = (byte)0x03;

    public static final byte CAL_DATE = (byte)0x01;
    public static final byte CAL_DATE_START = (byte)0x02;
    public static final byte CAL_DATE_END = (byte)0x03;

    FilterDialog filterDialog;

    FilterDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (filterDialog == null || !(filterDialog instanceof FilterDialog)) {
            filterDialog = new FilterDialog(getActivity());
        }
        Resources res = getResources();
        ((TextView) filterDialog.findViewById(R.id.dialogFilter_button_setDate)).
                setText(res.getString(R.string.text_label_byDate) + ": " + getDateString(CAL_DATE, Util.DATE_PATTERN));
        ((TextView) filterDialog.findViewById(R.id.dialogFilter_button_setDateRange)).
                setText(res.getString(R.string.text_label_byDateRange) + ": \n" +
                        getDateString(CAL_DATE_START, Util.DATE_PATTERN) + " - " + getDateString(CAL_DATE_END, Util.DATE_PATTERN));
        return filterDialog;
    }

    public byte getFilterMode(){
        if (filterDialog == null || !(filterDialog instanceof FilterDialog)) {
            return FILTER_ALL;
        }
        int buttonId = filterDialog.filterRG.getCheckedRadioButtonId();
        switch (buttonId) {
            case R.id.dialogFilter_button_setDate:
                return FILTER_BY_DATE;
            case R.id.dialogFilter_button_setDateRange:
                return FILTER_BY_DATE_RANGE;
            case R.id.dialogFilter_button_setAll:
                return FILTER_ALL;
            default:
                return FILTER_ALL;
        }
    }

    public String getDateString(byte iCal, String format) {
        Calendar cal;
        switch (iCal) {
            case CAL_DATE:
                cal = filterDialog.calDate;
                break;
            case CAL_DATE_START:
                cal = filterDialog.calDateStart;
                break;
            case CAL_DATE_END:
                cal = filterDialog.calDateEnd;
                break;
            default:
                return null;
        }
        return Util.formatDateString(cal, format);
    }

    public class FilterDialog extends Dialog {

        RadioGroup filterRG;
        FilterDatePickerFragment datePicker;
        Calendar calDate;
        Calendar calDateStart;
        Calendar calDateEnd;

        public FilterDialog(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.dialog_filter);
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            datePicker = new FilterDatePickerFragment();
            calDate = Calendar.getInstance();
            calDateStart = Calendar.getInstance();
            calDateEnd = Calendar.getInstance();
            filterRG = (RadioGroup) findViewById(R.id.dialogFilter_radioGroup);

            ((Button) findViewById(R.id.dialogFilter_button_setDate)).setOnClickListener(this::onClickSetDate);
            ((Button) findViewById(R.id.dialogFilter_button_setDateRange)).setOnClickListener(this::onClickSetDateRange);
            ((Button) findViewById(R.id.dialogFilter_button_setAll)).setOnClickListener(this::onClickSetAll);
            ((Button) findViewById(R.id.dialogFilter_button_search)).setOnClickListener(this::onClickSearch);
        }

        public void onClickSetDate(View v) {
            datePicker.setViewToUpdate(findViewById(R.id.dialogFilter_button_setDate));
            datePicker.setCal(calDate);
            datePicker.show(getParentFragmentManager(), FilterDatePickerFragment.TAG_SET_DATE);
        }

        public void onClickSetDateRange(View v) {
            datePicker.setViewToUpdate(findViewById(R.id.dialogFilter_button_setDateRange));
            datePicker.setCal(calDateStart);
            datePicker.cal2 = calDateEnd;
            datePicker.show(getParentFragmentManager(), FilterDatePickerFragment.TAG_SET_DATE_RANGE);
        }

        public void onClickSetAll(View v) {
        }

        public void onClickSearch(View v) {
            byte filterMode = FilterDialogFragment.this.getFilterMode();
            MainActivity.tabAdapter.recordsFragment.reloadRecords(filterMode, null);
            this.dismiss();
        }
    }

    public static class FilterDatePickerFragment extends DatePickerFragment {
        public static final String TAG_SET_DATE = "datePickerFilter";
        public static final String TAG_SET_DATE_RANGE = "datePickerFilterRange";

        Calendar cal2;
        boolean isStartDateSet = false;

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            View v = getViewToUpdate();
            Resources res = getResources();
            if (v != null && v instanceof TextView) {
                switch (this.getTag()) {
                    case TAG_SET_DATE:
                        super.onDateSet(view, year, month, day);
                        ((TextView) v).setText(res.getString(R.string.text_label_byDate) + ": " + Util.formatDateString(getCal()));
                        break;
                    case TAG_SET_DATE_RANGE:
                        if (!isStartDateSet){
                            // After selecting starting date
                            super.onDateSet(view, year, month, day);
                            ((TextView) v).setText(res.getString(R.string.text_label_byDateRange) + ": \n" +
                                    Util.formatDateString(getCal()) + " - " + Util.formatDateString(this.cal2));
                            isStartDateSet = true;
                        }
                        else {
                            // After selecting ending date
                            Calendar calStart = getCal();
                            this.setCal(this.cal2);
                            super.onDateSet(view, year, month, day);
                            ((TextView) v).setText(res.getString(R.string.text_label_byDateRange) + ": \n" +
                                    Util.formatDateString(calStart) + " - " + Util.formatDateString(this.cal2));
                            isStartDateSet = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}


