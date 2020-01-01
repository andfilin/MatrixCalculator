package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import android.widget.EditText;

import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hellow.R;


public class DisplayMatrixActivity extends AppCompatActivity {

    private int[][] matrix_A = null;
    private int[][] matrix_B = null;
    private int[][] matrix_Result = null;



    private static int nextId = 100;
    private int generate_id(){
        while(findViewById(nextId) != null){
            nextId++;
        }
        return nextId;
    }

    private void retrieveMatrices(){
        Intent intent = getIntent();
        Bundle matrixBundle = intent.getExtras();
        this.matrix_A = (int[][]) matrixBundle.getSerializable(MainActivity.EXTRA_MATRIX_A);
        this.matrix_B = (int[][]) matrixBundle.getSerializable(MainActivity.EXTRA_MATRIX_B);
    }

    private ScrollView makeScrollview(View content){
       // ConstraintLayout rootLayout = (ConstraintLayout) findViewById(R.id.display_rootview);
        ScrollView vertScroll = new ScrollView(this);
        HorizontalScrollView horScroll = new HorizontalScrollView(this);


        //rootLayout.addView(vertScroll);
        vertScroll.addView(horScroll);
        horScroll.addView(content);



        vertScroll.setLayoutParams(
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                )
        );



        vertScroll.setId(generate_id());

        return vertScroll;

        // constraints
        /*ConstraintSet cSet = new ConstraintSet();
        cSet.clone(rootLayout);
        cSet.connect(vertScroll.getId(), ConstraintSet.TOP, rootLayout.getId(), ConstraintSet.TOP, 160);
        cSet.connect(vertScroll.getId(), ConstraintSet.LEFT, rootLayout.getId(), ConstraintSet.LEFT, 160 );
        cSet.connect(vertScroll.getId(), ConstraintSet.RIGHT, rootLayout.getId(), ConstraintSet.RIGHT, 160 );
        cSet.applyTo(rootLayout);*/


    }



    private View makeMatrixRepresentation(int[][] m){
        View result = null;

        // make table with id
        TableLayout table = new TableLayout(this);
        table.setId(R.id.ahoy);
        // width and height
        table.setLayoutParams(
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                )
        );
        // fill table with rows
        for(int row = 0; row < m.length; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for(int col = 0; col < m[row].length; col++){
                // create edittext, inputtype = signedNumber
                final EditText value = new EditText(this);
                value.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_SIGNED);
                value.setText("" + m[row][col]);
                value.setTextSize(25);
                value.setPadding(10,0,10,0);
                // draw rectangle around edittext
                value.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tablecell));
                // onInput, update size of edittext with requestlayout
                value.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        value.requestLayout();

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


                /*value.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((TextView) v).setText("" + 42);
                        v.requestLayout();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

                    }
                });*/
                //value.setText("a");
                tableRow.addView(value);
                // id not unique for now.
               // value.setId(R.id.matElem);
                //value.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

            }
            table.addView(tableRow);
        }

        return table;

        /* add table to rootlayout
        // add view to layout
        ConstraintLayout rootLayout = (ConstraintLayout) findViewById(R.id.display_rootview);
        rootLayout.addView(table);
        // apply constraints
        ConstraintSet cSet = new ConstraintSet();
        cSet.clone(rootLayout);
        cSet.connect(table.getId(), ConstraintSet.TOP, rootLayout.getId(), ConstraintSet.TOP, 160);
        cSet.connect(table.getId(), ConstraintSet.LEFT, rootLayout.getId(), ConstraintSet.LEFT, 160 );
        cSet.connect(table.getId(), ConstraintSet.RIGHT, rootLayout.getId(), ConstraintSet.RIGHT, 160 );
        cSet.applyTo(rootLayout);
        */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_matrix);

        retrieveMatrices();


        View m1 = makeMatrixRepresentation(this.matrix_A);
        ScrollView scroll = makeScrollview(m1);

        ConstraintLayout rootLayout = (ConstraintLayout) findViewById(R.id.display_rootview);
        rootLayout.addView(scroll);
        // apply constraints
        ConstraintSet cSet = new ConstraintSet();
        cSet.clone(rootLayout);
        cSet.connect(scroll.getId(), ConstraintSet.TOP, rootLayout.getId(), ConstraintSet.TOP, 160);
        cSet.connect(scroll.getId(), ConstraintSet.LEFT, R.id.guideline_Mat, ConstraintSet.LEFT, 100 );
        //cSet.constrainWidth(scroll.getId(), 0);
        //cSet.connect(scroll.getId(), ConstraintSet.RIGHT, R.id.guideline_Mat, ConstraintSet.LEFT, 10 );
        cSet.applyTo(rootLayout);

        /*View m2 = makeMatrixRepresentation(this.matrix_A);
        ScrollView scroll2 = makeScrollview(m2);

        rootLayout.addView(scroll2);
        // apply constraints
        ConstraintSet cSet2 = new ConstraintSet();
        cSet2.clone(rootLayout);
        //cSet2.constrainWidth(scroll2.getId(), 300);
        cSet2.connect(scroll2.getId(), ConstraintSet.TOP, rootLayout.getId(), ConstraintSet.TOP, 160);
        //cSet2.connect(scroll2.getId(), ConstraintSet.RIGHT, rootLayout.getId(), ConstraintSet.RIGHT, 100 );
        cSet2.connect(scroll2.getId(), ConstraintSet.LEFT, R.id.guideline_Mat, ConstraintSet.RIGHT, 10 );
        cSet2.applyTo(rootLayout);*/



        // Log.println(Log.ERROR, "timeLog", "time to build table: " + (System.currentTimeMillis() - starttime));
        //makeScrollview();

        TextView mDims = (TextView) findViewById(R.id.matrixDims);
        mDims.setText("74");

    }
}
