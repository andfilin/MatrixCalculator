package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.hellow.Helper;
import com.example.hellow.OperationEnum;
import com.example.hellow.R;
import com.example.hellow.SelectiontypeEnum;

import org.ejml.simple.SimpleMatrix;

public class CalculationActivity extends AppCompatActivity {

    public final static String EXTRA_OPERATION = "extra_operation";

    private int[][] matrix_A = null;
    private int[][] matrix_B = null;
    private int[][] matrix_Result = null;
    private OperationEnum currentOperation = null;

    private void updateDisplay(int[][] matA, int[][] matB, int[][] matResult, OperationEnum operation){

        TableLayout table_matA = (TableLayout) findViewById(R.id.calculation_A);
        TableLayout table_matB = (TableLayout) findViewById(R.id.calculation_B);
        TableLayout table_matResult = (TableLayout) findViewById(R.id.calculation_R);
        TextView tv_operation = (TextView) findViewById(R.id.calculation_op);
        TextView tv_resultSymbol = (TextView) findViewById(R.id.calculation_equals);

        Helper.fillTable(this, table_matA, matA);
        Helper.fillTable(this, table_matB, matB);
        Helper.fillTable(this, table_matResult, matResult);

        char opSymbol = operation != null ? operation.getSymbol() : ' ';
        tv_operation.setText("" + opSymbol);

        char resultSymbol = matResult != null ? '=' : ' ';
        tv_resultSymbol.setText("" + resultSymbol);
    }

    public void doCalculation(View button){
        int[][] result;
        //result = libraray.doOp(matA, MatB, operation);
        SimpleMatrix S;
        result = new int[][]{
                {0}
        };
        this.matrix_Result = result;

        TableLayout table_matResult = (TableLayout) findViewById(R.id.calculation_R);
        Helper.fillTable(this, table_matResult, result);

        TextView tv_resultSymbol = (TextView) findViewById(R.id.calculation_equals);
        tv_resultSymbol.setText("" + '=');
    }

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




        updateDisplay(null, null, null, null);
        /*Helper.fillTable(this, matA, data);
        Helper.fillTable(this, matB, data2);
        Helper.fillTable(this, matR, data3);*/
    }

    /*
    * User clicked on an operation-button.
    * This starts MatrixSelectionActivity, where he can choose
    * one (Transpose, ...) or two (Addition, Multiplication, ...) Matrices from storage.
    * */
    public void onOperationSelected(View button){

        OperationEnum operation = OperationEnum.fromButton(button.getId());

        // start MatrixSelectionActivity, give selected operation in intent
        Intent selectOperandsIntent = new Intent(this, MatrixSelectionActivity.class);
        selectOperandsIntent.putExtra(EXTRA_OPERATION, operation);
        startActivityForResult(selectOperandsIntent, 1);
    }

    /*
    * On return from MatrixSelectionActivity, set Matrix
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){

                Log.e("onResult", "resultOk");
                // this bundle contains: int[][] EXTRA_RETURN_A,EXTRA_RETURN_B; OperationEnum EXTRA_OPERATION
                Bundle results = data.getExtras();
                int[][] returned_A = (int[][]) results.get(MatrixSelectionActivity.EXTRA_RETURN_A);
                int[][] returned_B = (int[][]) results.get(MatrixSelectionActivity.EXTRA_RETURN_B);
                OperationEnum returned_operation = (OperationEnum) results.get(EXTRA_OPERATION);

                // set variables
                this.matrix_A = returned_A;
                this.matrix_B = returned_B;
                this.currentOperation = returned_operation;

                // update display
                updateDisplay(this.matrix_A, this.matrix_B, null, this.currentOperation);

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
