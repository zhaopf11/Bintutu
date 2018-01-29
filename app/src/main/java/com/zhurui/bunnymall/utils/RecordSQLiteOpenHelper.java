package com.zhurui.bunnymall.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "bunnymall.db";
    private static Integer version = 1;
    private static RecordSQLiteOpenHelper mInstance = null;

    public static synchronized RecordSQLiteOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RecordSQLiteOpenHelper(context);
        }
        return mInstance;
    }
    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}