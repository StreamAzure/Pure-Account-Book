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
        String sql1 = "create table item(createTime text, reason int, account double, type int, date text,  year int, month int, day int)";
        //item表：创建日期，类型代号，金额，类型，账目日期，账目年，账目月，账目日
        String sql2 = "create table daysum(date text, income double, expend double)";
        //单日收支记录表daysum：账目日期，收入，支出
        String sql3 = "create table monthsum(year int, month int, income double, expend double)";
        //月收支记录表monthsum：账目年，账目月，收入，支出
        String sql4 = "create table yearsum(year int, income double, expend double)";
        //年收支记录表yearsum：账目年，收入，支出

        //触发器table_insert，在item插入新项前检查其他表中是否有对应项，没有则创建
        String sql5="CREATE TRIGGER table_insert BEFORE INSERT ON item\n" +
                "BEGIN\n" +
                "\tINSERT INTO yearsum (year,expend,income)\n" +
                "\tSELECT new.year,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * FROM yearsum WHERE year=new.year);\n" +
                "\tINSERT INTO monthsum (year,month,expend,income) \n" +
                "\tSELECT new.year,new.month,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * from monthsum where year=new.year and month=new.month);\n" +
                "\tINSERT INTO daysum (year,month,day,expend,income) \n" +
                "\tSELECT new.year,new.month,new.day,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * from daysum where year=new.year and month=new.month and day=new.day);\n" +
                "END";

        //触发器update_when_edit，在item更新后更新相关表
        String sql6="CREATE TRIGGER update_when_edit AFTER UPDATE ON item\n" +
                "BEGIN\n" +
                "\tUPDATE yearsum set expend = expend + new.account - old.account WHERE new.type = 0 AND year = new.year;\n" +
                "\tUPDATE yearsum set income = income + new.account - old.account WHERE new.type = 1 AND year = new.year;\n" +
                "\tUPDATE monthsum set expend = expend + new.account - old.account WHERE new.type = 0 AND year = new.year AND month = new.month;\n" +
                "\tUPDATE monthsum set income = income + new.account - old.account WHERE new.type = 1 AND year = new.year AND month = new.month;\n" +
                "\tUPDATE daysum set expend = expend + new.account - old.account WHERE new.type = 0 AND year = new.year AND month = new.month AND day = new.day;\n" +
                "\tUPDATE daysum set income = income + new.account - old.account WHERE new.type = 1 AND year = new.year AND month = new.month AND day = new.day;\n" +
                "END";

        //触发器update_when_insert，在item插入新项后更新相关表
        String sql7="CREATE TRIGGER update_when_insert after insert on item\n" +
                "BEGIN\n" +
                "\tUPDATE yearsum set expend = expend + new.account WHERE new.type = 0 AND year = new.year;\n" +
                "\tUPDATE yearsum set income = income + new.account WHERE new.type = 1 AND year = new.year;\n" +
                "\tupdate monthsum set expend = expend + new.account where new.type = 0 and year = new.year and month = new.month;\n" +
                "\tupdate monthsum set income = income + new.account where new.type = 1 and year = new.year and month = new.month;\n" +
                "\tupdate daysum set expend = expend + new.account where new.type = 0 and year = new.year and month = new.month and day = new.day;\n" +
                "\tupdate daysum set income = income + new.account where new.type = 1 and year = new.year and month = new.month and day = new.day;\n" +
                "END";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createTrigger(String triggerType, int type){
        //TODO:为什么不支持多语句的触发器，我不理解
    }
}
