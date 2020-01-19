package com.example.hellow.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hellow.OperationEnum;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private MatDBHelper matDBHelper;
    private Context context;
    private SQLiteDatabase database;

    public  DBManager(Context c){
        context = c;
    }

    /*
    * open DB
    * */
    public DBManager open(){
        matDBHelper = new MatDBHelper(context);
        database = matDBHelper.getWritableDatabase();
        return this;
    }
    /*
     * close DB
     * */
    public void close(){
        matDBHelper.close();
    }


    /*
    * serialize an Object to Byte[],
    * can be saved as BLOB in DB.
    * */
    public byte[] serialize(Object ob){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(ob);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    /*
    * deserialize a Byte[] (eg. BLOB in DB)
    * into Java Object.
    * */
    public Object deserialize(byte[] data){
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = null;
        Object result = null;
        try {
            is = new ObjectInputStream(in);
            result = is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * inserts a row into Matrix-table.
    * */
    public void insert_matrix(String name, double[][] data){

        // serialize data to bytes
        byte[] serializedData = serialize(data);
        
        
        int isScalar = (data.length == 1 && data[0].length == 1) ? 1 : 0;

        // put values
        ContentValues content = new ContentValues();
        content.put(matDBHelper.MATRIX_NAME, name);
        content.put(matDBHelper.MATRIX_DATA, serializedData);
        content.put(matDBHelper.MATRIX_ISSCALAR, isScalar);
        // insert
        database.insert(MatDBHelper.TABLE_MATRIX_NAME, null, content);
    }
    
    /*
    * inserts a row into History-table.
    * */
    public void insert_history(String name_leftoperand, double[][] data_leftperand, String name_rightoperand, double[][] data_rightperand, OperationEnum operation, double[][] data_result){

        // serialize data to bytes
        byte[] serialized_dataLeft = serialize(data_leftperand);
        byte[] serialized_dataRight = serialize(data_rightperand);
        byte[] serialized_dataResult = serialize(data_result);
        
        int operationValue = operation.getValue();
        
        // put values
        ContentValues content = new ContentValues();
        content.put(matDBHelper.HISTORY_OPERANDNAME_A, name_leftoperand);
        content.put(matDBHelper.HISTORY_OPERANDNAME_B, name_rightoperand);
        content.put(matDBHelper.HISTORY_OPERANDDATA_A, serialized_dataLeft);
        content.put(matDBHelper.HISTORY_OPERANDDATA_B, serialized_dataRight);
        content.put(matDBHelper.HISTORY_OPERATION, operationValue);
        content.put(matDBHelper.HISTORY_RESULTDATA, serialized_dataResult);
        // insert
        database.insert(MatDBHelper.TABLE_HISTORY_NAME, null, content);
    }

    public int deleteMatrices(String[] ids){
        if(ids.length == 0){
            return 0;
        }
        int rowsDeleted = database.delete(MatDBHelper.TABLE_MATRIX_NAME, MatDBHelper.MATRIX_ID + " IN (" + new String(new char[ids.length-1]).replace("\0", "?,") + "?)", ids);
        return rowsDeleted;
    }

    /*
    * select all rows with all cols and return cursor.
    * */
    private Cursor fetchAll(){
        Cursor cursor = database.query(MatDBHelper.TABLE_MATRIX_NAME, MatDBHelper.MATRIIX_COLS, null, null, null, null, null);
        return cursor;
    }

    /*
     * select only rows where isScalar == 1.
     * */
    private Cursor fetchScalars(){
        Cursor cursor = database.query(MatDBHelper.TABLE_MATRIX_NAME, MatDBHelper.MATRIIX_COLS, MatDBHelper.MATRIX_ISSCALAR + " = ", new String[]{1 + ""}, null, null, null);
        return cursor;
    }
    /*
     * select only rows where isScalar != 1.
     * */
    private Cursor fetchMatrices(){
        Cursor cursor = database.query(MatDBHelper.TABLE_MATRIX_NAME, MatDBHelper.MATRIIX_COLS, MatDBHelper.MATRIX_ISSCALAR + " != ", new String[]{1 + ""}, null, null, null);
        return cursor;
    }

    private Cursor fetchHistory(){
        Cursor cursor = database.query(MatDBHelper.TABLE_HISTORY_NAME, MatDBHelper.HISTORY_COLS, null, null, null, null, null);
        return cursor;
    }

    /*
     * For every Row in curor, builds Matrix-object and returns array thereof.
     * */
    private Matrix[] cursorToMatrixArray(Cursor cursor){
        // build list of Matrix for every row
        List<Matrix> result = new ArrayList<>();
        while(cursor.moveToNext()){
            // get values of current row
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.MATRIX_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MatDBHelper.MATRIX_NAME));
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.MATRIX_DATA));
            // deserialize 2d-array from blob
            double[][] data = (double[][]) deserialize(blob);

            boolean isScalar = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.MATRIX_ISSCALAR)) == 1;
            result.add(new Matrix(id, name, data, isScalar));
        }
        cursor.close();
        return result.toArray(new Matrix[result.size()]);
    }
    /*
     * For every Row in curor, builds History-object and returns array thereof.
     * */
    private HistoryEntry[] cursorToHistoryEntryArray(Cursor cursor){
        List<HistoryEntry> result = new ArrayList<>();
        // iterate cursor, make new HistoryEntry from every row
        while(cursor.moveToNext()){
            // left operand
            String nameLeft = cursor.getString(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_OPERANDNAME_A));
            double[][] dataLeft = (double[][]) deserialize(cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_OPERANDDATA_A)));
            // right operand
            String nameRight = cursor.getString(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_OPERANDNAME_B));
            double[][] dataRight = (double[][]) deserialize(cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_OPERANDDATA_B)));
            // operation
            int operationValue = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_OPERATION));
            OperationEnum operation = OperationEnum.fromInt(operationValue);
            // result
            double[][] dataResult = (double[][]) deserialize(cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.HISTORY_RESULTDATA)));

            //append new HistoryEntry
            result.add(new HistoryEntry(
                    nameLeft,
                    dataLeft,
                    nameRight,
                    dataRight,
                    operation,
                    dataResult
            ));
        }
        // close cursor and return array
        cursor.close();
        return result.toArray(new HistoryEntry[result.size()]);
    }

    /*
    * Returns array of Every Matrix and Scalar in DB
    * */
    public Matrix[] getAll(){
        Cursor cursor = fetchAll();
        return cursorToMatrixArray(cursor);
    }
    /*
    * Returns array of Every Scalar in DB
    * */
    public Matrix[] getScalars(){
        Cursor cursor = fetchScalars();
        return cursorToMatrixArray(cursor);
    }
    /*
     * Returns array of Every Matrix in DB (without scalars)
     * */
    public Matrix[] getMatrices(){
        Cursor cursor = fetchMatrices();
        return cursorToMatrixArray(cursor);
    }

    public HistoryEntry[] getHistory(){
        Cursor cursor = fetchHistory();
        return cursorToHistoryEntryArray(cursor);
    }

    /*
    * Returns true if a Matrix with the given name exists in DB.
    * */
    public boolean nameExists(String name){
        String[] cols = new String[] {MatDBHelper.MATRIX_ID, MatDBHelper.MATRIX_NAME};
        Cursor cursor = database.query(MatDBHelper.TABLE_MATRIX_NAME, cols, MatDBHelper.MATRIX_NAME + " = ?", new String[]{name + ""}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        boolean result = cursor.getCount() != 0;
        cursor.close();
        return result;



    }
}
