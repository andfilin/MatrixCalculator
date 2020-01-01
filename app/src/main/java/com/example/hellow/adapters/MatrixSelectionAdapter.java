package com.example.hellow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hellow.R;
import com.example.hellow.sqlite.Matrix;

public class MatrixSelectionAdapter extends RecyclerView.Adapter<MatrixSelectionAdapter.ViewHolder>{

    private Matrix[] dataset;
    private Context context;
    private int selectedMatrixPosition = -1;


    public MatrixSelectionAdapter(Matrix[] data, Context context){
        this.dataset = data;
        this.context = context;
    }

    public int getSelectedMatrixPosition(){
        return selectedMatrixPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectmatrix_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.radioButton.setText(dataset[position].getName());
        holder.radioButton.setChecked(selectedMatrixPosition == position);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.radioButton = (RadioButton) itemView.findViewById(R.id.selectItem_radio);

            this.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedMatrixPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

        }
    }

}
