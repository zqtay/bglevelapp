package com.example.myapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.util.Util;

public class RecordInfoDialog extends Dialog {
    String date;
    String event;
    String bglevel_pre;
    String bglevel_post;
    String dose;
    String notes;

    RecordInfoDialog(Context context, String date, String event, String bglevel_pre, String bglevel_post, String dose, String notes) {
        super(context);
        this.date = date;
        this.event = event;
        this.bglevel_pre = bglevel_pre;
        this.bglevel_post = bglevel_post;
        this.dose = dose;
        this.notes = notes;
        this.setContentView(R.layout.dialog_record);
        fillDialogContent();
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((Button) findViewById(R.id.dialog_button_delete)).setOnLongClickListener(this::onClickDelete);
        ((Button) findViewById(R.id.dialog_button_edit)).setOnClickListener(this::onClickEdit);
    }

    public void fillDialogContent() {
        ((TextView) findViewById(R.id.dialog_textView_date)).setText(date);
        ((TextView) findViewById(R.id.dialog_textView_event)).setText(event);
        ((TextView) findViewById(R.id.dialog_textView_bglevel_pre)).setText(bglevel_pre);
        ((TextView) findViewById(R.id.dialog_textView_bglevel_post)).setText(bglevel_post);
        ((TextView) findViewById(R.id.dialog_textView_dose)).setText(dose);
        ((TextView) findViewById(R.id.dialog_textView_notes)).setText(notes);
    }

    public boolean onClickDelete(View v) {
        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event);
        AppDatabaseService.deleteRecord(dataDate, dataEvent, v.getContext());
        return true;
    }

    public void onClickEdit(View v) {
        MainActivity.tabAdapter.formFragment.setFormValues(date, event, bglevel_pre, bglevel_post, dose, notes);
        MainActivity.viewPager.setCurrentItem(AppTabAdapter.TAB_FORM_POSITION);
        dismiss();
    }
}


