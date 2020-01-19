package com.example.hellow.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hellow.Helper;
import com.example.hellow.R;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.HistoryEntry;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
/*
* Used in HistoryActivity.
* */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private HistoryEntry[] dataset;
    private Context context;
    private boolean popup_open = false;

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

    private void showPopup(int position){

        if(popup_open){
            return;
        }

        HistoryEntry entry = dataset[position];

        // get root
        CardView parent = ((Activity) this.context).findViewById(R.id.history_popupParent);
        // inflate xml of popup
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_history, parent);



        // prepare content
        // set title
        TextView titleTv = (TextView) popupView.findViewById(R.id.history_popup_title);
        String title = entry.getLeftName() + " " + entry.getOperation().getSymbol() + " " +  (entry.getRightName() != null ? entry.getRightName() : "");
        titleTv.setText(title);

        // fill tables
        TableLayout tableA = popupView.findViewById(R.id.popup_history_A);
        Helper.fillTable(this.context, tableA, entry.getLeftData());
        TableLayout tableB = popupView.findViewById(R.id.popup_history_B);
        Helper.fillTable(this.context, tableB, entry.getRightData());
        TableLayout tableR = popupView.findViewById(R.id.popup_history_Result);
        Helper.fillTable(this.context, tableR, entry.getResultData());
        TextView opTv = (TextView) popupView.findViewById(R.id.popup_history_op);
        opTv.setText(entry.getOperation().getSymbol() + "");

        // button callbacks
        // closebutton
        ImageButton closebutton = popupView.findViewById(R.id.history_closebutton);
        closebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                popup_open = false;
            }
        });

        // savebutton
        Button saveButton = popupView.findViewById(R.id.history_savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open db
                DBManager dbManager = new DBManager(context);
                dbManager.open();

                // Get chosen name
                EditText nameInput = (EditText) popupView.findViewById(R.id.history_nameInput);
                String name = nameInput.getText().toString();
                // check if name is empty
                if(name.isEmpty()){
                    String msg = context.getResources().getString(R.string.newMatrix_emptyName);
                    Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                    toast.show();
                    // close db
                    dbManager.close();
                    return;
                }
                // check if name is already in use
                if(dbManager.nameExists(name)){
                    String msg = context.getResources().getString(R.string.newMatrix_duplicateName);
                    Toast toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                    // close db
                    dbManager.close();
                    return;
                }

                // insert matrix
                dbManager.insert_matrix(name, entry.getResultData());
                // success message
                String msg = context.getResources().getString(R.string.newMatrix_success);
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();

                // close db
                dbManager.close();
            }
        });

        popup_open = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameA;
        public TextView nameB;
        public TextView dimsA;
        public TextView dimsB;
        public TextView operation;

        public ImageButton viewMatrixButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nameA = itemView.findViewById(R.id.histItem_nameA);
            this.nameB = itemView.findViewById(R.id.histItem_nameB);
            this.dimsA = itemView.findViewById(R.id.histItem_dimsA);
            this.dimsB = itemView.findViewById(R.id.histItem_dimsB);
            this.operation = itemView.findViewById(R.id.histItem_operation);

            this.viewMatrixButton = (ImageButton) itemView.findViewById(R.id.history_item_viewbutton);
            viewMatrixButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(getAdapterPosition());
                }
            });
        }
    }

}
