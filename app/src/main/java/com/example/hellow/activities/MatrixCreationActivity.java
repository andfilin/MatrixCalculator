package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.hellow.Helper;
import com.example.hellow.R;
import com.example.hellow.sqlite.DBManager;

/*
* Allows user to create a Matrix and save it to the db.
* */
public class MatrixCreationActivity extends AppCompatActivity {
    // width of one edittext to hold value of matrix
    private final static int MATRIXVALUE_WIDTH = Helper.dpToPixels(60);

    private DBManager dbManager;

    // keeps track of currently input Matrix
    // private double[][] newMatrix;

    // keep track of chosen dimensions
    private int rowCount;
    private int colCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_creation);
        dbManager = new DBManager(this);
        dbManager.open();

        // set chosen dimensions to 3x3 and build table
        EditText rowsInput = (EditText) findViewById(R.id.createM_input_rows);
        EditText colsInput = (EditText) findViewById(R.id.createM_input_cols);
        rowsInput.setText("3");
        colsInput.setText("3");
        onApplyClicked(null);

    }

    private void initCreationTable(int rows, int cols){
        final int valueWidth = MATRIXVALUE_WIDTH;
        TableLayout table = (TableLayout) findViewById(R.id.CreateMatrix_Table);
        // fill table with rows
        for(int currentRow = 0; currentRow < rows; currentRow++){
            // init row, set params
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            // fill row with edittexts
            for(int currentCol = 0; currentCol < cols; currentCol++){
                EditText valInput = new EditText(this);
                valInput.setText("");

                TableRow.LayoutParams lparams =  new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                lparams.width = valueWidth;
                valInput.setLayoutParams(lparams);


                valInput.setEms(10);
                valInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                // when value changes, update int[][] newMatrix
                final int row = currentRow, col = currentCol;
               /* valInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Log.e("asd", "" + s);
                        if(s.length() != 0) {
                            newMatrix[row][col] = Double.parseDouble(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });*/




                // add edittext to row
                tr.addView(valInput);
            }
            // add row to table
            table.addView(tr);
        }
    }

    /*
    * Rebuilds InputMatrix according to chosen dimensions.
    * */
    public void onApplyClicked(View button){
        // get dimension inputs
        EditText rowsInput = (EditText) findViewById(R.id.createM_input_rows);
        EditText colsInput = (EditText) findViewById(R.id.createM_input_cols);

        // check for empty inputs
        if(rowsInput.getText().toString().isEmpty() || colsInput.getText().toString().isEmpty()){
            String msg = getResources().getString(R.string.newMatrix_invalidDimensions);
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        // parse dimensions to int
        this.rowCount = Integer.parseInt(rowsInput.getText().toString());
        this.colCount = Integer.parseInt(colsInput.getText().toString());

        // check for negative/0 input
        if(this.rowCount <= 0 || this.colCount <= 0){
            String msg = getResources().getString(R.string.newMatrix_invalidDimensions);
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // reset table
        ((TableLayout) findViewById(R.id.CreateMatrix_Table)).removeAllViews();

        // refill table
        initCreationTable(this.rowCount, this.colCount);
    }

    /*
    * Sets Dimensions of Matrix to 1x1.
    * */
    public void onScalarButtonClicked(View button){
        EditText rowsInput = (EditText) findViewById(R.id.createM_input_rows);
        rowsInput.setText("1");

        EditText colsInput = (EditText) findViewById(R.id.createM_input_cols);
        colsInput.setText("1");

        onApplyClicked(null);
    }

    /*
    * Saves input Matrix to DB.
    * */
    public void onSaveButtonClicked(View button){
        // Get chosen name
        EditText nameInput = (EditText) findViewById(R.id.createM_input_name);
        String name = nameInput.getText().toString();

        // check if name is empty
        if(name.isEmpty()){
            String msg = getResources().getString(R.string.newMatrix_emptyName);
            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // check if name is already in use
        if(dbManager.nameExists(name)){
            String msg = getResources().getString(R.string.newMatrix_duplicateName);
            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Get chosen Matrix by parsing table
        double[][] matrix = new double[this.rowCount][this.colCount];
        TableLayout table = (TableLayout) findViewById(R.id.CreateMatrix_Table);
        for(int row = 0; row < table.getChildCount(); row++){
            TableRow tRow = (TableRow) table.getChildAt(row);
            // iterate row
            for(int col = 0; col < tRow.getChildCount(); col++){
                EditText textinput = (EditText) tRow.getChildAt(col);
                String text = textinput.getText().toString();
                Double value = text.equals("") ? 0 : Double.parseDouble(text);
                matrix[row][col] = value;
            }
        }


        dbManager.insert_matrix(name, matrix);

        String msg = getResources().getString(R.string.newMatrix_success);
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();


    }

}
