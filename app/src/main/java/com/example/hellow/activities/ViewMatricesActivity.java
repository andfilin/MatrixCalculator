package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hellow.R;
import com.example.hellow.adapters.HistoryAdapter;
import com.example.hellow.adapters.ViewMatricesAdapter;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.Matrix;

import java.util.List;

public class ViewMatricesActivity extends AppCompatActivity {

    private ViewMatricesAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matrices);

        // get data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Matrix[] data = dbManager.getAll();
        dbManager.close();



        // prepare recyclerview
        RecyclerView rView = (RecyclerView) findViewById(R.id.viewMatrices_recyclerview);
        rView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter
        viewAdapter = new ViewMatricesAdapter(data, this);
        rView.setAdapter(viewAdapter);

        // add decoration
        DividerItemDecoration itemDecor = new DividerItemDecoration(rView.getContext(), DividerItemDecoration.VERTICAL);
        rView.addItemDecoration(itemDecor);

    }

    public void onDeleteButton(View button){
        List<String> checkedIds = viewAdapter.getCheckedIds();


        // delete data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        int delCount = dbManager.deleteMatrices(checkedIds.toArray(new String[checkedIds.size()]));
        viewAdapter.setDataset(dbManager.getAll());
        dbManager.close();

        // make toast
        String msg =  delCount == 1 ? delCount + " " + getResources().getString(R.string.item_deleted_toast) : delCount + " " + getResources().getString(R.string.items_deleted_toast);

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();

        viewAdapter.notifyDataSetChanged();



    }


}
