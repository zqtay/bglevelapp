package io.zqtay.bglevelapp.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import io.zqtay.bglevelapp.db.BGRecord;
import io.zqtay.bglevelapp.R;
import io.zqtay.bglevelapp.util.Util;

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

            cardView = (CardView) itemView.findViewById(R.id.record_cardView);
            dateTextView = (TextView) itemView.findViewById(R.id.record_view_date);
            eventTextView = (TextView) itemView.findViewById(R.id.record_view_event);
            bgLevelPreTextView = (TextView) itemView.findViewById(R.id.record_view_bgLevelPre);
            bgLevelPostTextView = (TextView) itemView.findViewById(R.id.record_view_bgLevelPost);
            doseTextView = (TextView) itemView.findViewById(R.id.record_view_dose);
            notesTextView = (TextView) itemView.findViewById(R.id.record_view_notes);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cardView = inflater.inflate(R.layout.fragment_record, parent, false);

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