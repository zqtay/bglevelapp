package com.example.myapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RecordsFragment extends Fragment {

    DatePickerFragment datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button reloadButton = (Button) view.findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(this::onClickReload);
        reloadButton.setOnLongClickListener(this::showDatePickerDialog);

        onClickReload(null);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                Activity activity = getActivity();
                View focusedView = null;
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                if ((focusedView = activity.getCurrentFocus()) != null) {
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }
            } catch (Exception e) {
                Log.e("error", "setUserVisibleHint: ", e);
            }
        }
    }

    public void onClickReload(View v) {
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

    public void onClickExportBackup(View v) {
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
            startActivityForResult(Intent.createChooser(i, "Choose directory"), Util.ACTIVITY_REQUEST_EXPORT_FILE_BACKUP);
        }
    }

    public void onClickExportExtended(View v) {
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
            startActivityForResult(Intent.createChooser(i, "Choose directory"), Util.ACTIVITY_REQUEST_EXPORT_FILE_EXTENDED);
        }
    }

    public void exportFile(Uri exportUri, byte exportFormat) {
        Context context = getActivity().getApplicationContext();
        String exportPath = FileUtil.getFullPathFromTreeUri(exportUri, context);
        Log.d("debug",exportPath);

        AsyncTask.execute(()-> {
            String recordsText = "";
            String fileName = "";
            List<BGRecord> records = AppDatabaseService.findAllRecord(context);
            switch (exportFormat) {
                case Util.EXPORT_FORMAT_BACKUP:
                    recordsText = Util.BGRECORD_HEADER + Util.getRecordsText(records);
                    fileName = Util.BGRECORD_EXPORT_FILENAME_BACKUP;
                    break;
                case Util.EXPORT_FORMAT_EXTENDED:
                    recordsText = Util.BGRECORD_HEADER_EXTENDED + Util.getRecordsTextExtended(records);
                    fileName = Util.BGRECORD_EXPORT_FILENAME_EXTENDED;
                    break;
                default:
                    break;
            }

            int exportResult = Util.writeToFile(recordsText, exportPath, fileName);
            String successMsg = "File exported to: " + exportPath + "/" + fileName;
            getActivity().runOnUiThread(() -> {
                if (exportResult == Util.RESULT_SUCCESS) {
                    Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "File export failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case Util.ACTIVITY_REQUEST_EXPORT_FILE_BACKUP:
                if (data != null) {
                    Uri exportUri = data.getData();
                    Log.i("Test", "Result URI " + exportUri);
                    exportFile(exportUri, Util.EXPORT_FORMAT_BACKUP);
                }
                break;
            case Util.ACTIVITY_REQUEST_EXPORT_FILE_EXTENDED:
                if (data != null) {
                    Uri exportUri = data.getData();
                    Log.i("Test", "Result URI " + exportUri);
                    exportFile(exportUri, Util.EXPORT_FORMAT_EXTENDED);
                }
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        Calendar lastSetCal;

        DatePickerFragment() {
            lastSetCal = Calendar.getInstance();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = lastSetCal.get(Calendar.YEAR);
            int month = lastSetCal.get(Calendar.MONTH);
            int day = lastSetCal.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Activity activity = getActivity();
            lastSetCal.set(Calendar.YEAR, year);
            lastSetCal.set(Calendar.MONTH, month);
            lastSetCal.set(Calendar.DAY_OF_MONTH, day);
            int date = Integer.parseInt((new SimpleDateFormat(Util.DATE_PATTERN_DATA)).format(lastSetCal.getTime()));
            AsyncTask.execute(()-> {
                List<BGRecord> records = AppDatabaseService.findRecordsByDate(date, activity.getApplicationContext());
                activity.runOnUiThread( ()-> {
                    RecyclerView rvRecords = (RecyclerView) activity.findViewById(R.id.rv_records);
                    RecordsAdapter adapter = new RecordsAdapter(records);
                    rvRecords.setAdapter(adapter);
                    rvRecords.setLayoutManager(new LinearLayoutManager(activity));
                });
            });
        }
    }

    public boolean showDatePickerDialog(View v) {
        FragmentActivity activity = getActivity();
        if (datePicker == null || !(datePicker instanceof RecordsFragment.DatePickerFragment)) {
            datePicker = new RecordsFragment.DatePickerFragment();
        }
        datePicker.show(activity.getSupportFragmentManager(), "datePickerRecords");
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_action_export_backup:
                onClickExportBackup(null);
                return true;
            case R.id.toolbar_menu_action_export_extended:
                onClickExportExtended(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
