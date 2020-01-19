package com.example.hellow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hellow.OperationEnum;
import com.example.hellow.R;
import com.example.hellow.SelectiontypeEnum;
import com.example.hellow.adapters.MatrixSelectionAdapter;
import com.example.hellow.sqlite.DBManager;
import com.example.hellow.sqlite.Matrix;

/*
* After operation is chosen, user can choose operands here.
* */
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
        int titleId = getResources().getIdentifier(operation.getStringResourceName(), "string", getPackageName());
        setTitle(getResources().getString(titleId));


        // get data
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        data = dbManager.getAll();
        dbManager.close();

        // init recyclers
        recycler_MatA = (RecyclerView) findViewById(R.id.select_A);
        recycler_MatA.setLayoutManager(new LinearLayoutManager(this));
        recycler_MatA.setAdapter(new MatrixSelectionAdapter(data, this));
        DividerItemDecoration itemDecorA = new DividerItemDecoration(recycler_MatA.getContext(), DividerItemDecoration.VERTICAL);
        recycler_MatA.addItemDecoration(itemDecorA);
        if(recycler_MatB != null){
            recycler_MatB.setLayoutManager(new LinearLayoutManager(this));
            recycler_MatB.setAdapter(new MatrixSelectionAdapter(data, this));
            DividerItemDecoration itemDecorB = new DividerItemDecoration(recycler_MatB.getContext(), DividerItemDecoration.VERTICAL);
            recycler_MatB.addItemDecoration(itemDecorB);
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

                int selectedIndex_A = ((MatrixSelectionAdapter) recycler_MatA.getAdapter()).getSelectedMatrixPosition();
                int selectedIndex_B = -1;
                if(recycler_MatB != null){
                    selectedIndex_B = ((MatrixSelectionAdapter) recycler_MatB.getAdapter()).getSelectedMatrixPosition();
                }
                // check whether Operand A was selected
                if(selectedIndex_A == -1){
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.operandA_notSelected), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                // check whether Operand B was selected and neccesary
                if(selectedIndex_B == -1 && recycler_MatB != null){
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.operandB_notSelected), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }


                if(selectedIndex_A != -1){
                    //results.putSerializable(EXTRA_RETURN_A, data[selectedIndex_A].getData());
                    results.putSerializable(EXTRA_RETURN_A, data[selectedIndex_A]);
                }
                // set selected Matrix B - if possible
                if(recycler_MatB != null){
                    selectedIndex_B = ((MatrixSelectionAdapter) recycler_MatB.getAdapter()).getSelectedMatrixPosition();
                    if(selectedIndex_B != -1){
                        //results.putSerializable(EXTRA_RETURN_B, data[selectedIndex_B].getData());
                        results.putSerializable(EXTRA_RETURN_B, data[selectedIndex_B]);
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
}
