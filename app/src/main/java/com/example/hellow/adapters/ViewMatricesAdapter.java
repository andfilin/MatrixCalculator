package com.example.hellow.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellow.Helper;
import com.example.hellow.R;
import com.example.hellow.sqlite.Matrix;

public class ViewMatricesAdapter extends RecyclerView.Adapter<ViewMatricesAdapter.ViewHolder>{

    private Matrix[] dataset;
    private Context context;
    private boolean popup_open = false;

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
        Matrix matrix = dataset[position];
        holder.itemName.setText(matrix.getName());
        holder.dimensions.setText(matrix.dimensionsString());
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    /*
    * open a popupwindow showing name and content of a matrix
    * */
    private void showPopup(String name, double[][] matData){

        if(popup_open){
            return;
        }

        // get root
        CardView parent = ((Activity) this.context).findViewById(R.id.view_popupParent);
        // inflate xml of popup
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view_matrices, parent);


        // set name
        TextView popup_name = (TextView) popupView.findViewById(R.id.popup_view_name);
        popup_name.setText(name);
        // fill table
        TableLayout popup_table = popupView.findViewById(R.id.popup_view_matrix);
        Helper.fillTable(this.context, popup_table, matData);

        // onclosebutton
        ImageButton closebutton = popupView.findViewById(R.id.popup_closebutton);
        closebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                popup_open = false;
            }
        });

        popup_open = true;
    }

    private void showPopup(int position){
        Matrix m = dataset[position];
        showPopup(m.getName(), m.getData());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        public TextView dimensions;
        public ImageButton viewMatrixButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.item_name);
            this.dimensions = itemView.findViewById(R.id.item_dimensions);
            this.viewMatrixButton = (ImageButton) itemView.findViewById(R.id.item_viewbutton);
            viewMatrixButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(getAdapterPosition());
                }
            });

        }





        }
    }


