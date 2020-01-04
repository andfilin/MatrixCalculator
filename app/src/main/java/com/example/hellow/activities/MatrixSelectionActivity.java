package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hellow.OperationEnum;
import com.example.hellow.R;
import com.example.hellow.SelectiontypeEnum;
import com.example.hellow.adapters.MatrixSelectionAdapter;
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

        // get selectiontype
        OperationEnum operation = (OperationEnum) this.getIntent().getSerializableExtra(CalculationActivity.EXTRA_OPERATION);
        SelectiontypeEnum selectionType = operation.getOperands();
        // set contentview according to selectiontype
        if(selectionType == SelectiontypeEnum.SINGLE){
            setContentView(R.layout.activity_matrix_selection_single);
        } else {
            setContentView(R.layout.activity_matrix_selection_double);
            recycler_MatB = (RecyclerView) findViewById(R.id.select_B);
        }

        // set title of activity to chosen operation
        setTitle(operation.toString());


        // get data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        data = dbManager.getAll();
        dbManager.close();

        // init recyclers
        recycler_MatA = (RecyclerView) findViewById(R.id.select_A);
        recycler_MatA.setLayoutManager(new LinearLayoutManager(this));
        recycler_MatA.setAdapter(new MatrixSelectionAdapter(data, this));
        if(recycler_MatB != null){
            recycler_MatB.setLayoutManager(new LinearLayoutManager(this));
            recycler_MatB.setAdapter(new MatrixSelectionAdapter(data, this));
        }
    }

    /*
    * callbacks for buttons 'ok' and 'cancel'
    * */
    public void onButtonClicked(View button){
        switch(button.getId()){
            // button 'ok'
            case R.id.select_button_ok:
                // prepare resultbundle
                Bundle results = new Bundle();
                // set selected Matrix A
                int selectedIndex_A = ((MatrixSelectionAdapter) recycler_MatA.getAdapter()).getSelectedMatrixPosition();
                if(selectedIndex_A != -1){
                    results.putSerializable(EXTRA_RETURN_A, data[selectedIndex_A].getData());
                }
                // set selected Matrix B - if possible
                if(recycler_MatB != null){
                    int selectedIndex_B = ((MatrixSelectionAdapter) recycler_MatB.getAdapter()).getSelectedMatrixPosition();
                    if(selectedIndex_B != -1){
                        results.putSerializable(EXTRA_RETURN_B, data[selectedIndex_B].getData());
                    }
                }
                // set selected operation
                OperationEnum operation = (OperationEnum) this.getIntent().getSerializableExtra(CalculationActivity.EXTRA_OPERATION);
                results.putSerializable(CalculationActivity.EXTRA_OPERATION, operation);

                // prepare intent and return
                Intent returnIntent = new Intent();
                returnIntent.putExtras(results);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.select_button_cancel:
                // return without results
                setResult(Activity.RESULT_CANCELED);
                finish();
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



    private Bundle generateReturn(SelectiontypeEnum selectType){

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
