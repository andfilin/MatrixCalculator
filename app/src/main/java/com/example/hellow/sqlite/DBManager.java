package com.example.hellow.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

    public DBManager open(){
        matDBHelper = new MatDBHelper(context);
        database = matDBHelper.getWritableDatabase();
       // matDBHelper.onUpgrade(database, 1, 1);
        return this;
    }

    public void close(){
        matDBHelper.close();
    }

    public byte[] serialize(Object ob) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(ob);
        return out.toByteArray();
    }

    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }


    public void insert(String name, double[][] data){

        // serialize array to bytes
        byte[] serializedData = new byte[0];
        try {
            serializedData = serialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int isScalar = (data.length == 1 && data[0].length == 1) ? 1 : 0;

        // put values
        ContentValues content = new ContentValues();
        content.put(matDBHelper.NAME, name);
        content.put(matDBHelper.DATA, serializedData);
        content.put(matDBHelper.IS_SCALAR, isScalar);


        database.insert(MatDBHelper.TABLE_NAME, null, content);
    }

    /*
    * select all rows with all cols and return cursor.
    * */
    private Cursor fetchAll(){
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME, MatDBHelper.DATA, MatDBHelper.IS_SCALAR};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, null, null, null, null, null);
        return cursor;
    }

    /*
     * select only rows where isScalar == 1.
     * */
    private Cursor fetchScalars(){
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME, MatDBHelper.DATA, MatDBHelper.IS_SCALAR};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, MatDBHelper.IS_SCALAR + " = ", new String[]{1 + ""}, null, null, null);
        return cursor;
    }
    /*
     * select only rows where isScalar != 1.
     * */
    private Cursor fetchMatrices(){
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME, MatDBHelper.DATA, MatDBHelper.IS_SCALAR};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, MatDBHelper.IS_SCALAR + " != ", new String[]{1 + ""}, null, null, null);
        return cursor;
    }

    /*
     * For every Row in curor, builds Matrix-object and returns array thereof.
     * */
    private Matrix[] cursorToArray(Cursor cursor){
        // build list of Matrix for every row
        List<Matrix> result = new ArrayList<>();
        while(cursor.moveToNext()){
            // get values of current row
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MatDBHelper.NAME));
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.DATA));
            // deserialize 2d-array from blob
            double[][] data = null;
            try {
                data = (double[][]) deserialize(blob);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            boolean isScalar = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.IS_SCALAR)) == 1;
            result.add(new Matrix(id, name, data, isScalar));
        }
        cursor.close();
        return result.toArray(new Matrix[result.size()]);
    }

    /*
    * Returns array of Every Matrix and Scalar in DB
    * */
    public Matrix[] getAll(){
        Cursor cursor = fetchAll();
        return cursorToArray(cursor);
    }
    /*
    * Returns array of Every Scalar in DB
    * */
    public Matrix[] getScalars(){
        Cursor cursor = fetchScalars();
        return cursorToArray(cursor);
    }
    /*
     * Returns array of Every Matrix in DB (without scalars)
     * */
    public Matrix[] getMatrices(){
        Cursor cursor = fetchMatrices();
        return cursorToArray(cursor);
    }

    /*
    * Returns true if a Matrix with the given name exists in DB.
    * */
    public boolean nameExists(String name){
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, MatDBHelper.NAME + " = ?", new String[]{name + ""}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        boolean result = cursor.getCount() != 0;
        cursor.close();
        return result;



    }

    /*
    * todo: unused
    *
    * */
    public Cursor find(String name){
        String[] names = new String[] {name};
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME, MatDBHelper.DATA,};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, MatDBHelper.NAME, names, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(long id){
        database.delete(MatDBHelper.TABLE_NAME, MatDBHelper.ID + "=" + id, null);
    }

    public String MatToString(int[][] mat){
        String res = "";
        for(int[] row : mat){
            for(int i : row){
                res += i + "\t";
            }
            res += "\n";
        }
        return res;
    }

}
