package com.example.hellow.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MatDBHelper extends SQLiteOpenHelper {
    // Table names
    public static final String TABLE_MATRIX_NAME = "Matrix";
    public static final String TABLE_HISTORY_NAME = "Chronik";

    // columns table matrix
    public static final String MATRIX_ID = "id";
    public static final String MATRIX_NAME = "name";
    public static final String MATRIX_DATA = "data";
    public static final String MATRIX_ISSCALAR = "is_scalar";

    public static final String[] MATRIIX_COLS = {
            MatDBHelper.MATRIX_ID,
            MatDBHelper.MATRIX_NAME,
            MatDBHelper.MATRIX_DATA,
            MatDBHelper.MATRIX_ISSCALAR
    };

    // columns table history
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_OPERANDNAME_A = "name_A";
    public static final String HISTORY_OPERANDDATA_A = "data_A";
    public static final String HISTORY_OPERANDNAME_B = "name_B";
    public static final String HISTORY_OPERANDDATA_B = "data_B";
    public static final String HISTORY_OPERATION     = "operation";
    public static final String HISTORY_RESULTDATA    = "data_result";

    public static final String[] HISTORY_COLS = {
            MatDBHelper.HISTORY_ID,
            MatDBHelper.HISTORY_OPERANDNAME_A,
            MatDBHelper.HISTORY_OPERANDNAME_B,
            MatDBHelper.HISTORY_OPERANDDATA_A,
            MatDBHelper.HISTORY_OPERANDDATA_B,
            MatDBHelper.HISTORY_OPERATION,
            MatDBHelper.HISTORY_RESULTDATA
    };
    public static final int VERSION = 1;

    // createtable-strings
    private static final String CREATE_TABLE_MATRIX = "create table " + TABLE_MATRIX_NAME + "("
            + MATRIX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MATRIX_NAME + " TEXT UNIQUE, "
            + MATRIX_DATA + " BLOB, "
            + MATRIX_ISSCALAR + " INTEGER"
            + ");"
            ;

    private static final String CREATE_TABLE_HISTORY = "create table " + TABLE_HISTORY_NAME + "("
            + HISTORY_ID +              " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HISTORY_OPERANDNAME_A +   " TEXT, "
            + HISTORY_OPERANDDATA_A +   " BLOB, "
            + HISTORY_OPERANDNAME_B +   " TEXT, "
            + HISTORY_OPERANDDATA_B +   " BLOB, "
            + HISTORY_OPERATION +       " INTEGER, "
            + HISTORY_RESULTDATA +      " BLOB "
            + ");"
            ;

    public MatDBHelper(Context context) {
        super(context, MATRIX_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MATRIX);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATRIX_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_NAME);
        onCreate(db);
    }
}
