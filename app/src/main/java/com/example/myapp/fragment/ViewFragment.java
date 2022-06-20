package com.example.myapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.util.FileUtil;
import com.example.myapp.R;
import com.example.myapp.util.Util;
import com.example.myapp.db.AppDatabaseService;
import com.example.myapp.db.BGRecord;

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

        view.findViewById(R.id.button_findAll).setOnClickListener(this::onClickFindAll);
        view.findViewById(R.id.button_export).setOnClickListener(this::onClickExport);

        onClickFindAll(view);
    }

    public void onClickFindAll(View v) {
        Activity activity = getActivity();

        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(activity.getApplicationContext());
            getActivity().runOnUiThread( ()-> {
                RecyclerView rvRecords = (RecyclerView) activity.findViewById(R.id.rv_records);
                RecordsAdapter adapter = new RecordsAdapter(records);
                rvRecords.setAdapter(adapter);
                rvRecords.setLayoutManager(new LinearLayoutManager(activity));
            });
        });
    }

    public void onClickExport(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), Util.ACTIVITY_REQUEST_EXPORT_FILE);
        }
    }

    public void exportFile(Uri exportUri) {
        Activity activity = getActivity();
        Context context = getActivity().getApplicationContext();
        String exportPath = FileUtil.getFullPathFromTreeUri(exportUri, context);
        Log.d("debug",exportPath);
        AsyncTask.execute(()-> {
            List<BGRecord> records = AppDatabaseService.findAllRecord(activity.getApplicationContext());
            String textDisplay = Util.BGRECORD_HEADER + Util.getRecordsText(records);
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
            case Util.ACTIVITY_REQUEST_EXPORT_FILE:
                Uri exportUri = data.getData();
                Log.i("Test", "Result URI " + exportUri);
                if (exportUri != null) {
                    exportFile(exportUri);
                }
                break;
        }
    }
}
