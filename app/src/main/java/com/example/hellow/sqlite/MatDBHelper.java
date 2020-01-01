package com.example.hellow.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MatDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Matrix";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATA = "data";

    public static final int VERSION = 1;

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + DATA + " BLOB" +
            ");";






    public MatDBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
