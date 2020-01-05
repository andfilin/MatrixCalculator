package com.example.hellow.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellow.Helper;
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
        Matrix matrix = dataset[position];
        holder.radioButton.setText(matrix.getName());
        holder.dimensions.setText(matrix.dimensionsString());
        holder.radioButton.setChecked(selectedMatrixPosition == position);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    /*
     * open a popupwindow showing name and content of a matrix
     * */
    private void showPopup(String name, double[][] matData){
        // get root
        View parent = ((Activity) this.context).findViewById(R.id.select_root);
        // inflate xml of popup
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view_matrices, null);
        // set name
        TextView popup_name = (TextView) popupView.findViewById(R.id.popup_view_name);
        popup_name.setText(name);
        // fill table
        TableLayout popup_table = popupView.findViewById(R.id.popup_view_matrix);
        Helper.fillTable(this.context, popup_table, matData);
        // show popup
        final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    private void showPopup(int position){
        Matrix m = dataset[position];
        showPopup(m.getName(), m.getData());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RadioButton radioButton;
        public TextView dimensions;
        public ImageButton viewMatrixButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.radioButton = (RadioButton) itemView.findViewById(R.id.selectItem_radio);
            this.dimensions = itemView.findViewById(R.id.selectItem_dimensions);
            this.viewMatrixButton = (ImageButton) itemView.findViewById(R.id.selectItem_view);

            this.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedMatrixPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

            this.viewMatrixButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(getAdapterPosition());
                }
            });



        }
    }

}
