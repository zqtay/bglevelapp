package com.example.myapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.R;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(getActivity().getApplicationContext());
            getActivity().runOnUiThread( ()-> {
                String textDisplay = getRecordsText(records);
                if (textDisplay.isEmpty()) {
                    textDisplay = "No records found";
                }
                else {
                    textDisplay = "date,event,bglevel_pre,bglevel_post,dose,notes\n" + textDisplay;
                }

                ((TextView) view.findViewById(R.id.textView_view)).setText(textDisplay);
            });
        });

        view.findViewById(R.id.button_findAll).setOnClickListener(this::onClickFindAll);

    }

    private String getRecordsText(List<BGRecord> records) {
        String recordsText = "";
        String recordLine = "";

        if (records == null) {
            return recordsText;
        }

        for (int i = 0; i < records.size(); i++) {
            recordLine = "";
            recordLine += String.valueOf(records.get(i).date);
            recordLine += (",");
            recordLine += String.valueOf(records.get(i).event);
            recordLine += (",");
            recordLine += (records.get(i).bglevel_pre != null) ? String.format("%.01f", records.get(i).bglevel_pre) : null;
            recordLine += (",");
            recordLine += (records.get(i).bglevel_post != null) ? String.format("%.01f", records.get(i).bglevel_post) : null;
            recordLine += (",");
            recordLine += (records.get(i).dose != null) ? String.format("%.01f", records.get(i).dose) : null;
            recordLine += (",");
            recordLine += records.get(i).notes;
            recordLine += ("\n");
            recordsText += recordLine;
        }

        return recordsText;
    }

    public void onClickFindAll(View v) {
        Activity activity = getActivity();
        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(activity.getApplicationContext());
            getActivity().runOnUiThread( ()-> {
                String textDisplay = getRecordsText(records);
                if (textDisplay.isEmpty()) {
                    textDisplay = "No records found";
                }
                else {
                    textDisplay = "date,event,bglevel_pre,bglevel_post,dose,notes\n" + textDisplay;
                }

                ((TextView) activity.findViewById(R.id.textView_view)).setText(textDisplay);
            });
        });
    }
}
