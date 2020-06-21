package com.pengxh.easywallpaper.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pengxh.easywallpaper.bean.StarNameBean;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"StaticFieldLeak"})
public class SQLiteUtil {
    private static final String TAG = "SQLiteUtil";
    private static Context context;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "EasyWallpaper.db";
    /**
     * 数据库表名
     */
    private static final String HISTORY = "HistoryTable";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;
    private SQLiteDatabase db;
    private static volatile SQLiteUtil sqLiteUtil = null;

    public static void initDataBase(Context mContext) {
        context = mContext.getApplicationContext();
    }

    private SQLiteUtil() {
        SQLiteUtilHelper mSqLiteUtilHelper = new SQLiteUtilHelper(context, DB_NAME, null, VERSION);
        db = mSqLiteUtilHelper.getWritableDatabase();
    }

    public static SQLiteUtil getInstance() {
        if (null == sqLiteUtil) {
            synchronized (SQLiteUtil.class) {
                if (null == sqLiteUtil) {
                    sqLiteUtil = new SQLiteUtil();
                }
            }
        }
        return sqLiteUtil;
    }

    public void saveStar(String name) {
        if (!isStarExist(name)) {
            ContentValues values = new ContentValues();
            values.put("starName", name);
            db.insert(HISTORY, null, values);
        } else {
            Log.d(TAG, "『" + name + "』已存在");
        }
    }

    public List<StarNameBean> loadAllHistory() {
        List<StarNameBean> list = new ArrayList<>();
        Cursor cursor = db.query(HISTORY, null, null, null, null, null, "id DESC");//倒序
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StarNameBean resultBean = new StarNameBean();
            resultBean.setStarName(cursor.getString(cursor.getColumnIndex("starName")));
            list.add(resultBean);
            //下一次循环开始
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void deleteAll() {
        db.delete(HISTORY, null, null);
    }

    private boolean isStarExist(String selectionArgs) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.query(HISTORY, null, "starName = ?", new String[]{selectionArgs}, null, null, null);
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}
