package com.example.hellow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Helper {

    /*
    * Update an TableLayout to display an int[][]
    * */
    public static void fillTable(Context context, TableLayout table, int[][] matData){
        // reset table
        table.removeAllViews();
        // if no data given, display empty matrix - for now, 3x3 Nullmatrix
        if(matData == null){
            return;
            /*matData = new int[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0},
            };*/
        }
        // iterate rows of data
        for(int[] matRow : matData){
            // init tablerow, set params
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            // iterate values of datarow
            for(int matValue : matRow){
                // append new textview to tablerow
                TextView textview = new TextView(context);
                textview.setText("" + matValue);
                int fontsize = (int) context.getResources().getDimension(R.dimen.matrix_fontsize);
                textview.setTextSize(fontsize);

                textview.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                textview.setBackgroundResource(R.drawable.tablecell);

                tr.addView(textview);

            }
            table.addView(tr);
        }

        // table.setBackground??
        table.setBackgroundResource(R.drawable.matrixborder);


    }
}
