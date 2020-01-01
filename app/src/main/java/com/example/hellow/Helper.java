package com.example.hellow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Helper {


    public static void fillTable(Context context, TableLayout table, int[][] matData){
        table.removeAllViews();
        for(int[] matRow : matData){
            // init row, set params
            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
            for(int matValue : matRow){
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
