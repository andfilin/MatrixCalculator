package com.example.hellow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ejml.data.DMatrixRMaj;

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

    public static double[][] intMat_to_doubleMat(int[][] data){
        double[][] result = new double[data.length][data[0].length];
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[0].length; j++){
                result[i][j] = data[i][j];
            }
        }
        return result;
    }

    public static int[][] mat_to_2dArray(DMatrixRMaj mat){
        int rows = mat.numRows;
        int cols = mat.numCols;
        int[][] result = new int[rows][cols];
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                result[row][col] = (int) mat.getData()[cols * row + col];
            }
        }
        return result;
    }

}
