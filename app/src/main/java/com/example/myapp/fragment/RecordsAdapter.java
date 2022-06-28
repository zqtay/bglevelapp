package com.example.myapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.db.BGRecord;
import com.example.myapp.util.Util;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {

    public List<BGRecord> records;

    public RecordsAdapter(List<BGRecord> records) {
        this.records = records;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView dateTextView;
        public TextView eventTextView;
        public TextView bgLevelPreTextView;
        public TextView bgLevelPostTextView;
        public TextView doseTextView;
        public TextView notesTextView;

        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dateTextView = (TextView) itemView.findViewById(R.id.card_textView_date);
            eventTextView = (TextView) itemView.findViewById(R.id.card_textView_event);
            bgLevelPreTextView = (TextView) itemView.findViewById(R.id.card_textView_bglevel_pre);
            bgLevelPostTextView = (TextView) itemView.findViewById(R.id.card_textView_bglevel_post);
            doseTextView = (TextView) itemView.findViewById(R.id.card_textView_dose);
            notesTextView = (TextView) itemView.findViewById(R.id.card_textView_notes);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cardView = inflater.inflate(R.layout.fragment_cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        BGRecord record = records.get(position);

        String date = Util.formatDateString(String.valueOf(record.date));
        String event = Util.convertEvent(record.event);
        String bglevel_pre = (record.bglevel_pre != null) ? String.format("%.01f", record.bglevel_pre) : "";
        String bglevel_post = (record.bglevel_post != null) ? String.format("%.01f", record.bglevel_post) : "";
        String dose = (record.dose != null) ? String.format("%.01f", record.dose) : "";
        String notes = (record.notes != null) ? record.notes : "";

        holder.dateTextView.setText(date);
        holder.eventTextView.setText(event);
        holder.bgLevelPreTextView.setText(bglevel_pre);
        holder.bgLevelPostTextView.setText(bglevel_post);
        holder.doseTextView.setText(dose);
        holder.notesTextView.setText(notes);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordInfoDialog dialog = new RecordInfoDialog(view.getContext(), date, event, bglevel_pre, bglevel_post, dose, notes);
                //dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}