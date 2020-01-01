package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.hellow.R;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.MatDBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MatrixCreationActivity extends AppCompatActivity {

    private DBManager dbManager;

    private int[][] newMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_creation);
        dbManager = new DBManager(this);
        dbManager.open();
    }

    private int dpToPixels(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        //float px = dp * (metrics.densityDpi / 160f);

        return Math.round(dp * metrics.density);
    }


    private void initCreationTable(int rows, int cols){
        final int valueWidth = dpToPixels(60);
        TableLayout table = (TableLayout) findViewById(R.id.CreateMatrix_Table);
        // fill table with rows
        for(int currentRow = 0; currentRow < rows; currentRow++){
            // init row, set params
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            // fill row with edittext
            for(int currentCol = 0; currentCol < cols; currentCol++){
                EditText valInput = new EditText(this);
                valInput.setText("0");

                TableRow.LayoutParams lparams =  new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                lparams.width = valueWidth;
                valInput.setLayoutParams(lparams);


                valInput.setEms(10);
                valInput.setInputType(InputType.TYPE_CLASS_NUMBER);

                // when value changes, update int[][] newMatrix
                final int row = currentRow, col = currentCol;
                valInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Log.e("asd", "" + s);
                        if(s.length() != 0) {
                            newMatrix[row][col] = Integer.parseInt(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });




                // add edittext to row
                tr.addView(valInput);
            }
            // add row to table
            table.addView(tr);
        }
    }

    public void onButtonClicked(View button){
        ((TableLayout) findViewById(R.id.CreateMatrix_Table)).removeAllViews();
        EditText rowsInput = (EditText) findViewById(R.id.createM_input_rows);
        int rows = Integer.parseInt(rowsInput.getText().toString());

        EditText colsInput = (EditText) findViewById(R.id.createM_input_cols);
        int cols = Integer.parseInt(colsInput.getText().toString());

        if(rows < 0 || cols < 0){
            return;
        }

        initCreationTable(rows, cols);
        newMatrix = new int[rows][cols];

    }

    public void onSaveButtonClicked(View button){
        EditText nameInput = (EditText) findViewById(R.id.createM_input_name);
        String name = nameInput.getText().toString();

        dbManager.insert(name, newMatrix);

        Toast toast = Toast.makeText(getApplicationContext(), "Matrix gespeichert", Toast.LENGTH_SHORT);
        toast.show();


    }

}
