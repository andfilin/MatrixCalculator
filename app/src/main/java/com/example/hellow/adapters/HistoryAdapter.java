package com.example.hellow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellow.R;
import com.example.hellow.sqlite.HistoryEntry;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private HistoryEntry[] dataset;
    private Context context;

    public HistoryAdapter(HistoryEntry[] dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryEntry entry = dataset[position];
        holder.nameA.setText(entry.getLeftName());
        holder.nameB.setText(entry.getRightName());
        holder.dimsA.setText(entry.getDimsLeft());
        holder.dimsB.setText(entry.getDimsRight());
        holder.operation.setText(entry.getOperation().getSymbol() + "");
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameA;
        public TextView nameB;
        public TextView dimsA;
        public TextView dimsB;
        public TextView operation;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nameA = itemView.findViewById(R.id.histItem_nameA);
            this.nameB = itemView.findViewById(R.id.histItem_nameB);
            this.dimsA = itemView.findViewById(R.id.histItem_dimsA);
            this.dimsB = itemView.findViewById(R.id.histItem_dimsB);
            this.operation = itemView.findViewById(R.id.histItem_operation);

            // onclick
        }
    }

}
