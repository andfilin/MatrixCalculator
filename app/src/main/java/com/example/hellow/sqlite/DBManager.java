package com.example.hellow.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        //matDBHelper.onUpgrade(database, 1, 1);
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

    public void insert(String name, int[][] data){
        // serialize array to bytes
        byte[] serializedData = new byte[0];
        try {
            serializedData = serialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // put values
        ContentValues content = new ContentValues();
        content.put(matDBHelper.NAME, name);
        content.put(matDBHelper.DATA, serializedData);
        database.insert(MatDBHelper.TABLE_NAME, null, content);
    }

    public Cursor fetch(){
        String[] cols = new String[] {MatDBHelper.ID, MatDBHelper.NAME, MatDBHelper.DATA,};
        Cursor cursor = database.query(MatDBHelper.TABLE_NAME, cols, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Matrix[] fetch2(){
        Cursor cursor = fetch();
        //Matrix[] result = new Matrix[cursor.getCount()];
        List<Matrix> result = new ArrayList<>();
        //int index = 0;
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MatDBHelper.ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MatDBHelper.NAME));
            byte[] blob = cursor.getBlob(cursor.getColumnIndexOrThrow(MatDBHelper.DATA));
            int[][] data = null;
            try {
                data = (int[][]) deserialize(blob);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
          //  result[index] = new Matrix(id, name, data);
            //index++;
            result.add(new Matrix(id, name, data));
        }
        return result.toArray(new Matrix[result.size()]);
    }

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
