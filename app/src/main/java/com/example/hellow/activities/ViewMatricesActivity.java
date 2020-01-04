package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.hellow.R;
import com.example.hellow.adapters.ViewMatricesAdapter;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.Matrix;

public class ViewMatricesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matrices);

        // get data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Matrix[] data = dbManager.getAll();
        dbManager.close();
        Log.e("db", "fetchedsize: " + data.length);




        RecyclerView rView = (RecyclerView) findViewById(R.id.viewMatrices_recyclerview);
        rView.setLayoutManager(new LinearLayoutManager(this));

        rView.setAdapter(new ViewMatricesAdapter(data, this));

    }


}
