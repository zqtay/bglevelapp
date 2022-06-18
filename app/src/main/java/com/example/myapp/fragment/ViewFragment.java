package com.example.myapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.FileUtil;
import com.example.myapp.R;
import com.example.myapp.Util;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewFragment extends Fragment {
    public static final int ACTIVITY_REQUEST_EXPORT_FILE = 0x9999;
    public static final String BGRECORD_HEADER = "date,event,bglevel_pre,bglevel_post,dose,notes\n";

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

        view.findViewById(R.id.button_findAll).setOnClickListener(this::onClickFindAll);
        view.findViewById(R.id.button_export).setOnClickListener(this::onClickExport);

        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(getActivity().getApplicationContext());
            getActivity().runOnUiThread( ()-> {
                String textDisplay = Util.getRecordsText(records);
                if (textDisplay.isEmpty()) {
                    textDisplay = "No records found";
                }
                else {
                    textDisplay = BGRECORD_HEADER + textDisplay;
                }

                ((TextView) view.findViewById(R.id.textView_view)).setText(textDisplay);
            });
        });

        view.findViewById(R.id.button_findAll).setOnClickListener(this::onClickFindAll);

    }

    public void onClickFindAll(View v) {
        Activity activity = getActivity();
        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(activity.getApplicationContext());
            getActivity().runOnUiThread( ()-> {
                String textDisplay = Util.getRecordsText(records);
                if (textDisplay.isEmpty()) {
                    textDisplay = "No records found";
                }
                else {
                    textDisplay = BGRECORD_HEADER + textDisplay;
                }

                ((TextView) activity.findViewById(R.id.textView_view)).setText(textDisplay);
            });
        });
    }

    public void onClickExport(View v) {
        Activity activity = getActivity();
        Context context = activity.getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), ACTIVITY_REQUEST_EXPORT_FILE);
        }


    }

    public void exportFile(Uri exportUri) {
        Activity activity = getActivity();
        Context context = getActivity().getApplicationContext();
        String exportPath = FileUtil.getFullPathFromTreeUri(exportUri, context);
        Log.d("debug",exportPath);
        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(activity.getApplicationContext());
            String textDisplay = BGRECORD_HEADER + Util.getRecordsText(records);
            int exportResult = Util.writeToFile(textDisplay, exportPath, "BGRecord.txt");
            getActivity().runOnUiThread(() -> {
                if (exportResult == Util.RESULT_SUCCESS) {
                    Toast.makeText(context,
                                    "File exported to: " + exportPath + "\\" + "BGRecord.txt",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Toast.makeText(context,
                                    "File export failed",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case ACTIVITY_REQUEST_EXPORT_FILE:
                Uri exportUri = data.getData();
                Log.i("Test", "Result URI " + exportUri);
                if (exportUri != null) {
                    exportFile(exportUri);
                }
                break;
        }
    }
}
