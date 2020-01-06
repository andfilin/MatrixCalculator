package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.hellow.R;
import com.example.hellow.adapters.HistoryAdapter;
import com.example.hellow.adapters.ViewMatricesAdapter;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.HistoryEntry;
import com.example.hellow.sqlite.Matrix;

// todo: recyclerview showing historyentrys
//          popup for saving result to main memory
//          maybe delete history after app closes
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DBManager dbManager = new DBManager(this);
        dbManager.open();
        HistoryEntry[] history = dbManager.getHistory();
        dbManager.close();

        // prepare recyclerview
        RecyclerView rView = (RecyclerView) findViewById(R.id.history_reclclerview);
        rView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter
        rView.setAdapter(new HistoryAdapter(history, this));

        // add decoration
        DividerItemDecoration itemDecor = new DividerItemDecoration(rView.getContext(), DividerItemDecoration.VERTICAL);
        rView.addItemDecoration(itemDecor);


    }
}
