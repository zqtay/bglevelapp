package com.example.myapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapp.R;

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
    }

    public void fillDialogContent() {
        ((TextView) findViewById(R.id.dialog_textView_date)).setText(date);
        ((TextView) findViewById(R.id.dialog_textView_event)).setText(event);
        ((TextView) findViewById(R.id.dialog_textView_bglevel_pre)).setText(bglevel_pre);
        ((TextView) findViewById(R.id.dialog_textView_bglevel_post)).setText(bglevel_post);
        ((TextView) findViewById(R.id.dialog_textView_dose)).setText(dose);
        ((TextView) findViewById(R.id.dialog_textView_notes)).setText(notes);
    }
}


