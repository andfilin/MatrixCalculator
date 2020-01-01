package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.example.hellow.Helper;
import com.example.hellow.R;
import com.example.hellow.SelectionType;

public class CalculationActivity extends AppCompatActivity {

    public final static String EXTRA_SELECTIONTYPE = "extra_selectiontype";

    private int[][] matrix_A = null;
    private int[][] matrix_B = null;
    private int[][] matrix_Result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        TableLayout matA = (TableLayout) findViewById(R.id.calculation_A);
        TableLayout matB = (TableLayout) findViewById(R.id.calculation_B);
        TableLayout matR = (TableLayout) findViewById(R.id.calculation_R);
        int[][] data = new int[][] {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}
        };
        int[][] data2 = new int[][] {
                {1, 2},
                {1, 2},
                {1, 2},
                {1, 2},
                {1, 2},
                {1, 2},
        };
        int[][] data3 = new int[][] {
                {1, 2, 4, 5, 6, 7, 7},
                {1, 2, 4, 5, 6, 7, 7},
                {1, 2, 4, 5, 6, 7, 7},
                {1, 2, 4, 5, 6, 7, 7},
                {1, 2, 4, 5, 6, 7, 7}
        };





        Helper.fillTable(this, matA, data);
        Helper.fillTable(this, matB, data2);
        Helper.fillTable(this, matR, data3);
    }

    public void onOperationSelected(View button){

        SelectionType selectType = null;
        char opSymbol;

        switch(button.getId()){
            case R.id.calculation_add:
                selectType = SelectionType.TWO;
                opSymbol = '+';
                break;

            case R.id.calculation_transpose:
                selectType = SelectionType.SINGLE;
                opSymbol = 'T';
                break;

            case R.id.calculation_scalar:
                selectType = SelectionType.SCALAR;
                opSymbol = '*';
                break;
        }

        Intent selectOperandsIntent = new Intent(this, MatrixSelectionActivity.class);
        selectOperandsIntent.putExtra(EXTRA_SELECTIONTYPE, selectType.toInt());
        startActivityForResult(selectOperandsIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Log.e("onResult", "resultOk");
                Bundle results = data.getExtras();
                for(String key : results.keySet()){
                    Log.e("onResult", "extra: " + key + " = " + (results.get(key) != null ? ((int[][])results.get(key)).length : "NULL"));
                }
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.e("onResult", "resultCacneled");
            }

        }
    }

    public void navigateButton(View button){
        Intent intent = null;
        switch (button.getId()){
            case R.id.calculation_openViewMatrices:
                intent = new Intent(this, ViewMatricesActivity.class);
                startActivity(intent);
                break;
            case R.id.calculation_openCreateMatrix:
                intent = new Intent(this, MatrixCreationActivity.class);
                startActivity(intent);
                break;
                case R.id.calculation_openHistory:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
        }

    }






}
