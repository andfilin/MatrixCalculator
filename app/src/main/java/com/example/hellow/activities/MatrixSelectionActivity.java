package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hellow.R;
import com.example.hellow.SelectionType;
import com.example.hellow.adapters.MatrixSelectionAdapter;
import com.example.hellow.adapters.ViewMatricesAdapter;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.Matrix;


public class MatrixSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_RETURN_A = "extra_return_A";
    public static final String EXTRA_RETURN_B = "extra_return_B";

    private RecyclerView recycler_MatA;
    private RecyclerView recycler_MatB;

    private Matrix[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SelectionType selectionType = SelectionType.fromInt(
                this.getIntent().getIntExtra(CalculationActivity.EXTRA_SELECTIONTYPE, 1)
        );

        //RecyclerView recycler_MatA;
        //RecyclerView recycler_MatB = null;

        if(selectionType == SelectionType.SINGLE){
            setContentView(R.layout.activity_matrix_selection_single);
        } else {
            setContentView(R.layout.activity_matrix_selection_double);
            recycler_MatB = (RecyclerView) findViewById(R.id.select_B);
        }

        // get data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        data = dbManager.fetch2();
        dbManager.close();


        recycler_MatA = (RecyclerView) findViewById(R.id.select_A);
        recycler_MatA.setLayoutManager(new LinearLayoutManager(this));
        recycler_MatA.setAdapter(new MatrixSelectionAdapter(data, this));
        if(recycler_MatB != null){
            recycler_MatB.setLayoutManager(new LinearLayoutManager(this));
            recycler_MatB.setAdapter(new MatrixSelectionAdapter(data, this));
        }




        /*switch(selectionType){
            case SINGLE:
                setContentView(R.layout.activity_matrix_selection_single);
                break;
            case TWO:
                setContentView(R.layout.activity_matrix_selection_double);

                break;
            case SCALAR:
                setContentView(R.layout.activity_matrix_selection_double);
                break;
        }*/


        //finish();
    }

    public void onButtonClicked(View button){
        switch(button.getId()){
            case R.id.select_button_ok:
                // ...

                int selectedIndex = ((MatrixSelectionAdapter) recycler_MatA.getAdapter()).getSelectedMatrixPosition();
                if(selectedIndex != -1){
                    Bundle results = setResults(data[selectedIndex].getData());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(results);
                    setResult(Activity.RESULT_OK, returnIntent);


                }
                finish();
                break;
            case R.id.select_button_cancel:
                    break;

        }
    }

    private Bundle setResults(int[][] selectionA){
        Bundle results = new Bundle();
        results.putSerializable(EXTRA_RETURN_A, selectionA);
        return results;
    }

    private Bundle setResults(int[][] selectionA, int[][] selectionB){
        Bundle results = setResults(selectionA);
        results.putSerializable(EXTRA_RETURN_B, selectionB);
        return results;
    }



    private Bundle generateReturn(SelectionType selectType){

        Bundle results = new Bundle();

        int[][] m1 = new int[][] {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };

        int[][] m2 = new int[][] {
                {3},
                {3},
                {3}
        };

        int[][] s1 = new int[][]{
                {5}
        };

        switch (selectType){
            case TWO:
                results.putSerializable(EXTRA_RETURN_A, m1);
                results.putSerializable(EXTRA_RETURN_B, m2);
                break;
            case SINGLE:
                results.putSerializable(EXTRA_RETURN_A, m1);
                break;
            case SCALAR:
                results.putSerializable(EXTRA_RETURN_A, s1);
                results.putSerializable(EXTRA_RETURN_B, m2);
                break;
        }

        return results;


    }

}
