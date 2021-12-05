package com.jnu.pureaccount.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static Integer Version = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //context:上下文 name：数据库名称 factory:可选的游标工厂（通常是NULL） version：数据库版本，值必须为整数且递增
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLite 没有一个单独的用于存储日期和/或时间的存储类，但 SQLite 能够把日期和时间存储为 TEXT值
        String sql1 = "create table item(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, reason int, account double, date text, type int)";
        //String sql2 = "create table daysum(date text, income double, expend double)";
        //创建单日收支记录表
        db.execSQL(sql1);
        //执行sql语句
        //db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
