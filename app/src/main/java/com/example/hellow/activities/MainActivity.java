package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hellow.R;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MATRIX = "extramatrix";
    public final static String EXTRA_MATRIX_A = "extramatrix_A";
    public final static String EXTRA_MATRIX_B = "extramatrix_B";
    public final static String EXTRA_MATRIX_Result = "extramatrix_Result";

    public void onButtonClicked(View view){
        Log.println(Log.ASSERT, "x", "clicked, " + System.currentTimeMillis());
        // the matrix to pass - for now as 2D-array
        /*int[][] m =
                {
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9},
                        {10, 11, 12},
                };
        int[][] m2 =
                {
                        {1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                        {1, 2, 3, 4, 9999999, 4, 4, 4, 4, 4, 4, 4},
                        {1, 2, 3, 4, 4, 4, 4, 4, 256, 4, 4, 4}
                };
        */
        int[][] m_empty = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        // put matrix in a -Bundle- object; key: EXTRA_MATRIX
        Bundle matrixBundle = new Bundle();
        matrixBundle.putSerializable(EXTRA_MATRIX_A, m_empty);
        matrixBundle.putSerializable(EXTRA_MATRIX_B, m_empty);
        // intent to start -DisplayMatrixActivity-
        //Intent intent = new Intent(this, DisplayMatrixActivity.class);
        //Intent newMatrix_intent = new Intent(this, MatrixCreationActivity.class);
        // throw the bundle in
        //intent.putExtras(matrixBundle);

        // #-- enter the matrix --#
        //startActivity(newMatrix_intent);
        Intent intent;
        if(view.getId() == R.id.button_continue){
            //intent = new Intent(this, MatrixCreationActivity.class);
            intent = new Intent(this, CalculationActivity.class);
            startActivity(intent);
        } else if(view.getId() ==  R.id.viewM__Button){
            intent = new Intent(this, ViewMatricesActivity.class);
            startActivity(intent);
        } else if(view.getId() ==  R.id.button_main){
            intent = new Intent(this, CalculationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button continue = (Button)

        Log.println(Log.ASSERT, "x", "onCreate, " + System.currentTimeMillis());


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.println(Log.ASSERT, "x", "onStart, " + System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.println(Log.ASSERT, "x", "onResume, " + System.currentTimeMillis());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.println(Log.ASSERT, "x", "onPause, " + System.currentTimeMillis());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.println(Log.ASSERT, "x", "onStop, " + System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.println(Log.ASSERT, "x", "onDestroy, " + System.currentTimeMillis());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.println(Log.ASSERT, "x", "onRestart, " + System.currentTimeMillis());
    }
}
