package com.example.hellow.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellow.Helper;
import com.example.hellow.R;
import com.example.hellow.sqlite.Matrix;

public class ViewMatricesAdapter extends RecyclerView.Adapter<ViewMatricesAdapter.ViewHolder>{

    private Matrix[] dataset;
    private Context context;
    public ViewMatricesAdapter(Matrix[] data, Context context){
        this.dataset = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_matrices_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemName.setText(dataset[position].getName());
        //Helper.fillTable(this.context, holder.table, dataset[position].getData());

        /*final Matrix finalMat = dataset[position];
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("viewMatrices", "elem " + finalPos + " clicked!!");
                showPopup(finalMat.getName(), finalMat.getData());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    private void showPopup(String name, int[][] matData){

        View parent = ((Activity) this.context).findViewById(R.id.viewMatrices_recyclerview);


        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view_matrices, null);

        // set popup-content
        TextView popup_name = (TextView) popupView.findViewById(R.id.popup_view_name);
        popup_name.setText(name);

        TableLayout popup_table = popupView.findViewById(R.id.popup_view_matrix);
        Helper.fillTable(this.context, popup_table, matData);


        final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    private void showPopup(int position){
        Matrix m = dataset[position];
        showPopup(m.getName(), m.getData());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        //private TableLayout table;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.item_name);
            //this.table = itemView.findViewById(R.id.item_table);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(getAdapterPosition());
                }
            });

        }





        }
    }


