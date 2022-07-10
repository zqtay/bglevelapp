package com.example.myapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapp.MainActivity;
import com.example.myapp.R;

public class FilterDialogFragment extends DialogFragment {

    FilterDialog filterDialog;

    // TODO get current selection

    FilterDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (filterDialog == null || !(filterDialog instanceof FilterDialog)) {
            filterDialog = new FilterDialog(getActivity());
        }
        return filterDialog;
    }


    public static class FilterDialog extends Dialog {

        RadioGroup filterRG;

        public FilterDialog(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.dialog_filter);
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ((Button) findViewById(R.id.dialogFilter_button_setDate)).setOnClickListener(this::onClickSetDate);
            // TODO
            //((Button) findViewById(R.id.dialogFilter_button_setDateRange)).setOnClickListener(this::onClickSetDateRange);
            ((Button) findViewById(R.id.dialogFilter_button_setAll)).setOnClickListener(this::onClickSetAll);
            filterRG = (RadioGroup) findViewById(R.id.dialogFilter_radioGroup);
        }

        public void onClickSetDate(View v) {
            MainActivity.tabAdapter.recordsFragment.showDatePickerDialog(null);
            this.dismiss();
        }

        public void onClickSetDateRange(View v) {
            this.dismiss();
        }

        public void onClickSetAll(View v) {
            MainActivity.tabAdapter.recordsFragment.reloadAllRecords();
            this.dismiss();
        }
    }
}


