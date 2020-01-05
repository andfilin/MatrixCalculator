package com.example.hellow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ejml.data.DMatrixRMaj;

import java.util.Locale;

/*
* various static helperfunctions
* */
public class Helper {

    /*
    * Update an TableLayout to display an double[][]
    * */
    public static void fillTable(Context context, TableLayout table, double[][] matData){
        // reset table
        table.removeAllViews();
        // if no data given, display nothing
        if(matData == null){
            return;
        }

        // iterate rows of data
        for(double[] matRow : matData){
            // init tablerow, set params
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            // iterate values of datarow
            for(double matValue : matRow){
                // append new textview to tablerow
                TextView textview = new TextView(context);
                // make formatted String from double: up to 3 decimal points and no trailing zeros
                String valueString = String.format(Locale.US, "%.3f", matValue).replaceAll("\\.?0*$", "");
                textview.setText(valueString);

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

    public static double[][] mat_to_2dArray(DMatrixRMaj mat){
        int rows = mat.numRows;
        int cols = mat.numCols;
        double[][] result = new double[rows][cols];
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                result[row][col] = mat.getData()[cols * row + col];
            }
        }
        return result;
    }

    /*
     * converts deviceindependent 'dp'-unit to absolute pixels on this device
     * */
    public static int dpToPixels(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        //float px = dp * (metrics.densityDpi / 160f);
        return Math.round(dp * metrics.density);
    }

}
