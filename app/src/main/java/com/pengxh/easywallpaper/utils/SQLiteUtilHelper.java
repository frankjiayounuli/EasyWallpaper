package com.pengxh.easywallpaper.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteUtilHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteUtilHelper";
    private static final String SQL_HISTORY = "create table HistoryTable(id integer primary key autoincrement,starName text)";

    SQLiteUtilHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_HISTORY);
        Log.d(TAG, "数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
