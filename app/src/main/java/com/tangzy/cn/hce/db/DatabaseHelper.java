package com.tangzy.cn.hce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tang on 2015/8/27.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public static final int DB_VERSION = 4;
    public static final String DB_NAME = "hce.db";

    private String TABLE_LOG = "log";
    public static final String TABLE_ID_COL = "_ID";// 主键
    public static final String TABLE_NAME_COL = "timeMillis";// 时间戳
    public static final String TABLE_CODE = "codeNum";// 未知数
    public static final String TABLE_ATC = "atc";// atc

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LOG + " (" + //
                TABLE_ID_COL + " integer primary key autoincrement, " + //
                TABLE_NAME_COL + " VARCHAR(20),"+TABLE_CODE+" VARCHAR(16),"+TABLE_ATC+" VARCHAR(8))"//
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {

    }

    public void insert(){

    }





}
