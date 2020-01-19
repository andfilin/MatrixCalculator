package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hellow.Helper;
import com.example.hellow.OperationEnum;
import com.example.hellow.R;
import com.example.hellow.SelectiontypeEnum;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.Matrix;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;
/*
* Mainactivity.
* User can choose and execute Operation.
* */
public class CalculationActivity extends AppCompatActivity {
    // Extra-key for giving chosen operation to MatrixselectionActivity
    public final static String EXTRA_OPERATION = "extra_operation";
    // currently chosen Operands, Operation and Result
    private Matrix matrix_A = null;
    private Matrix matrix_B = null;
    private double[][] matrix_Result = null;
    private OperationEnum currentOperation = null;

    /*
    * Displays chosen Operands, Operation, Result in upper half of screen.
    * */
    private void updateDisplay(double[][] matA, double[][] matB, double[][] matResult, OperationEnum operation){
        // get all Views to update
        TableLayout table_matA = (TableLayout) findViewById(R.id.calculation_A);
        TableLayout table_matB = (TableLayout) findViewById(R.id.calculation_B);
        TableLayout table_matResult = (TableLayout) findViewById(R.id.calculation_R);
        TextView tv_operation = (TextView) findViewById(R.id.calculation_op);
        TextView tv_resultSymbol = (TextView) findViewById(R.id.calculation_equals);
        // fill tables with matrices
        Helper.fillTable(this, table_matA, matA);
        Helper.fillTable(this, table_matB, matB);
        Helper.fillTable(this, table_matResult, matResult);
        // set operation-symbol
        char opSymbol = operation != null ? operation.getSymbol() : ' ';
        tv_operation.setText("" + opSymbol);
        // set '='
        char resultSymbol = matResult != null ? '=' : ' ';
        tv_resultSymbol.setText("" + resultSymbol);
    }
    /*
    * callback for 'Ausfuehren'-Button.
    * does various checks, calculates result, updates display.
    * */
    public void doCalculation(View button){
        // check whether any operation chosen
        if(currentOperation == null){
            Toast toast = Toast.makeText(this, getResources().getString(R.string.noSelection), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        double[][] result;
        // prepare ejml-matrices
        DMatrixRMaj matA = new DMatrixRMaj(this.matrix_A.getData());
        DMatrixRMaj matB = null;
        if(this.currentOperation.getOperands() != SelectiontypeEnum.SINGLE){
             matB = new DMatrixRMaj(this.matrix_B.getData());
        }
        // initialize ejml-matrix to hold result with correct dimensions
        DMatrixRMaj matR = prepareResultmatrix(this.currentOperation, matA, matB);
        if(matR == null){
            // if prepareResultmatrix() returns null, dimensions of operands do not fit operation
            return;
        }

        double rank;
        double det;
        // make calculation
        switch(this.currentOperation){
            case ADD:
                CommonOps_DDRM.add(matA,matB,matR);
                break;
            case SUBTRACT:
                CommonOps_DDRM.subtract(matA,matB,matR);
                break;
            case MULTIPLY:
                CommonOps_DDRM.mult(matA,matB,matR);
                break;
            case SCALE:
                CommonOps_DDRM.scale(this.matrix_B.getData()[0][0], matA, matR);
                break;
            case TRANSPOSE:
                CommonOps_DDRM.transpose(matA, matR);
                break;
            case INVERT:
                // if det == 0, no Inverse exists
                det = CommonOps_DDRM.det(matA);
                if(det == 0){
                    displayMessage(getResources().getString(R.string.no_inverse));
                    return;
                }
                CommonOps_DDRM.invert(matA, matR);
                break;
            case DETERMINANT:
                det = CommonOps_DDRM.det(matA);
                matR.set(0, 0, det);
                break;
            case RANK:
                rank = MatrixFeatures_DDRM.rank(matA);
                matR.set(0, 0, rank);
                break;
            case TO_POWER:
                double n = this.matrix_B.getData()[0][0];
                matR = matA.copy();
                for(int i = 0; i < n - 1; i++){
                    DMatrixRMaj tmp = matR.copy();
                    CommonOps_DDRM.mult(tmp, matA, matR);
                }
                break;
        }

        // convert ejml-matrixobject to 2d-doubblearray
        result = Helper.mat_to_2dArray(matR);
        this.matrix_Result = result;

        // display result
        TableLayout table_matResult = (TableLayout) findViewById(R.id.calculation_R);
        Helper.fillTable(this, table_matResult, result);
        // disply '='
        TextView tv_resultSymbol = (TextView) findViewById(R.id.calculation_equals);
        tv_resultSymbol.setText("" + '=');


        // save operation and matrices to history-dbtable
        DBManager dbManager = new DBManager(this);
        dbManager.open();

        String nameB = null;
        double[][] dataB = null;
        if(matrix_B !=null){
            nameB = matrix_B.getName();
            dataB = matrix_B.getData();
        }

        dbManager.insert_history(
                matrix_A.getName(),
                matrix_A.getData(),
                nameB,
                dataB,
                this.currentOperation,
                this.matrix_Result
        );

        dbManager.close();

    }

    /*
    * Asserts dimensions of operands match operation and returns Matrix to hold result.
    * If dims do not match operation, displays message onscreen and returns null;
    * */
    private DMatrixRMaj prepareResultmatrix(OperationEnum operation, DMatrixRMaj matA, DMatrixRMaj matB){

        switch(operation){
            // dims identical
            case ADD:
            case SUBTRACT:
                if( (matA.numCols != matB.numCols) || (matA.numRows != matB.numRows) ) {
                    displayMessage(getResources().getString(R.string.addSub_wrongDims));
                    return null;
                }
                return new DMatrixRMaj(matA.numRows, matA.numCols);
               // break;
            // a.cols = b.rows
            // r.rows = a.rows, r.cols = b.cols
            case MULTIPLY:
                if(matA.numCols != matB.numRows){
                    displayMessage(getResources().getString(R.string.mult_wrongDims));
                    return null;
                }
                return new DMatrixRMaj(matA.numRows, matB.numCols);
                //break;
            // b.rows == b.cols == 1
            // r.dims = a.dims
            case SCALE:
                if (matB.data.length > 1) {
                    displayMessage(getResources().getString(R.string.not_a_scalar));
                    return null;
                }
                return new DMatrixRMaj(1, 1);
            //break;
            // r = a
            case TRANSPOSE:
                return new DMatrixRMaj(matA.numCols, matA.numRows);
            case INVERT:

                if (matA.numRows != matA.numCols) {
                    displayMessage(getResources().getString(R.string.mustBeSquare));
                    return null;
                }
                return new DMatrixRMaj(matA.numRows, matA.numCols);
           // break;
            // a -> square
            // r -> 1x1
            case DETERMINANT:
                if (matA.numRows != matA.numCols) {
                    displayMessage(getResources().getString(R.string.mustBeSquare));
                    return null;
                }
                return new DMatrixRMaj(1, 1);
            //break;
            // r -> 1x1
            case RANK:
                return new DMatrixRMaj(1, 1);
            //break;
            // a -> square
            case TO_POWER:
                if (matA.numRows != matA.numCols) {
                    displayMessage(getResources().getString(R.string.mustBeSquare));
                    return null;
                }
                if (matB.numRows != 1 || matB.numCols != 1) {
                    displayMessage(getResources().getString(R.string.not_a_scalar));
                    return null;
                }
                return new DMatrixRMaj(matA.numRows, matA.numCols);
           // break;
            default:
                displayMessage("unkonwn operation");
                return null;
        }
    }
    /*
    * display toast.
    * */
    private void displayMessage(String msg){
        Log.e("asd", msg);
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        // initially, show nothing
        updateDisplay(null, null, null, null);
    }

    /*
    * User clicked on an operation-button.
    * This starts MatrixSelectionActivity, where he can choose
    * one (Transpose, ...) or two (Addition, Multiplication, ...) Matrices from storage.
    * */
    public void onOperationSelected(View button){
        // get operation from id of pressed button
        OperationEnum operation = OperationEnum.fromButton(button.getId());

        // start MatrixSelectionActivity, give selected operation in intent
        Intent selectOperandsIntent = new Intent(this, MatrixSelectionActivity.class);
        selectOperandsIntent.putExtra(EXTRA_OPERATION, operation);
        startActivityForResult(selectOperandsIntent, 1);
    }

    /*
    * On return from MatrixSelectionActivity, set chosen operands and operation.
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                // this bundle contains: int[][] EXTRA_RETURN_A,EXTRA_RETURN_B; OperationEnum EXTRA_OPERATION
                Bundle results = data.getExtras();
                Matrix returned_A = (Matrix) results.get(MatrixSelectionActivity.EXTRA_RETURN_A);
                Matrix returned_B = (Matrix) results.get(MatrixSelectionActivity.EXTRA_RETURN_B);
                OperationEnum returned_operation = (OperationEnum) results.get(EXTRA_OPERATION);

                // set variables
                this.matrix_A = returned_A;
                this.matrix_B = returned_B;
                this.currentOperation = returned_operation;

                // update display
                updateDisplay(this.matrix_A.getData(), this.matrix_B != null ? this.matrix_B.getData():null, null, this.currentOperation);

            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.e("onResult", "resultCanceled");
            }
        }
    }
    /*
    * callback for buttons leading to other Actiivities
    * (ViewMatricesActivity, MatrixCreationActivity, HistoryActivity)
    * */
    public void navigateButton(View button){
        Intent intent = null;
        // start activty depending on id of pressedd button
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
