package io.zqtay.bglevelapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.zqtay.bglevelapp.db.AppDatabaseService;
import io.zqtay.bglevelapp.MainActivity;
import io.zqtay.bglevelapp.R;
import io.zqtay.bglevelapp.util.Util;

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

        ((Button) findViewById(R.id.dialogRecord_button_delete)).setOnLongClickListener(this::onClickDelete);
        ((Button) findViewById(R.id.dialogRecord_button_edit)).setOnClickListener(this::onClickEdit);
    }

    public void fillDialogContent() {
        ((TextView) findViewById(R.id.dialogRecord_view_date)).setText(date);
        ((TextView) findViewById(R.id.dialogRecord_view_event)).setText(event);
        ((TextView) findViewById(R.id.dialogRecord_view_bgLevelPre)).setText(bglevel_pre);
        ((TextView) findViewById(R.id.dialogRecord_view_bgLevelPost)).setText(bglevel_post);
        ((TextView) findViewById(R.id.dialogRecord_view_dose)).setText(dose);
        ((TextView) findViewById(R.id.dialogRecord_view_notes)).setText(notes);
    }

    public boolean onClickDelete(View v) {
        int dataDate = Util.convertDate(date);
        byte dataEvent = Util.convertEvent(event);
        AppDatabaseService.deleteRecord(dataDate, dataEvent, v.getContext());
        return true;
    }

    public void onClickEdit(View v) {
        MainActivity.tabAdapter.formFragment.setFormValues(date, event, bglevel_pre, bglevel_post, dose, notes);
        MainActivity.viewPager.setCurrentItem(AppTabAdapter.TAB_INDEX_FORM);
        dismiss();
    }
}


