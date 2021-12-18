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
        String sql1 = "create table item(createTime text, reason int, account double, type int, date text,  year int, month int, day int, remarks text)";
        //item表：创建日期，类型代号，金额，类型，账目日期，账目年，账目月，账目日，备注
        String sql2 = "create table daysum(year int, month int, day int, income double, expend double)";
        //单日收支记录表daysum：账目日期，收入，支出
        //千万不要动这个顺序！！查询的时候按列查询的！！
        String sql3 = "create table monthsum(year int, month int, income double, expend double)";
        //月收支记录表monthsum：账目年，账目月，收入，支出
        String sql4 = "create table yearsum(year int, income double, expend double)";
        //年收支记录表yearsum：账目年，收入，支出

        //触发器table_insert，在item 插入 新项前检查其他表中是否有对应项，没有则创建
        String sql5="CREATE TRIGGER year_insert BEFORE INSERT ON item\n" +
                "BEGIN\n" +
                "\tINSERT INTO yearsum (year,expend,income)\n" +
                "\tSELECT new.year,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * FROM yearsum WHERE year=new.year);END\n";

        String sql6 = "CREATE TRIGGER month_insert BEFORE INSERT ON item\n" +
                 "BEGIN\n" +
                "\tINSERT INTO monthsum (year,month,expend,income) \n" +
                "\tSELECT new.year,new.month,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * from monthsum where year=new.year and month=new.month);END\n";

        String sql7 = "CREATE TRIGGER day_insert BEFORE INSERT ON item\n" +
                "BEGIN\n" +
                "\tINSERT INTO daysum (year,month,day,expend,income) \n" +
                "\tSELECT new.year,new.month,new.day,0,0\n" +
                "\tWHERE NOT EXISTS(SELECT * from daysum where year=new.year and month=new.month and day=new.day);\n" +
                "END";


        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);

        //触发器，在item 插入 后更新其他表数据
        createInsertTrigger(db,"year_update_when_insert_e","expend",0,"yearsum");
        createInsertTrigger(db,"year_update_when_insert_i","income",1,"yearsum");
        createInsertTrigger(db,"month_update_when_insert_e","expend",0,"monthsum");
        createInsertTrigger(db,"month_update_when_insert_i","income",1,"monthsum");
        createInsertTrigger(db,"day_update_when_insert_e","expend",0,"daysum");
        createInsertTrigger(db,"day_update_when_insert_i","income",1,"daysum");

        //触发器，在item 更新 后更新其他表的数据
        createUpdateTrigger(db,"year_update_when_edit_e","expend",0,"yearsum");
        createUpdateTrigger(db,"year_update_when_edit_i","income",1,"yearsum");
        createUpdateTrigger(db,"month_update_when_edit_e","expend",0,"monthsum");
        createUpdateTrigger(db,"month_update_when_edit_i","income",1,"monthsum");
        createUpdateTrigger(db,"day_update_when_edit_e","expend",0,"daysum");
        createUpdateTrigger(db,"day_update_when_edit_i","income",1,"daysum");

        //触发器，在item 删除 后更新其他表的数据
        createDeleteTrigger(db,"year_update_when_del_e","expend",0,"yearsum");
        createDeleteTrigger(db,"year_update_when_del_i","income",1,"yearsum");
        createDeleteTrigger(db,"month_update_when_del_e","expend",0,"monthsum");
        createDeleteTrigger(db,"month_update_when_del_i","income",1,"monthsum");
        createDeleteTrigger(db,"day_update_when_del_e","expend",0,"daysum");
        createDeleteTrigger(db,"day_update_when_del_i","income",1,"daysum");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createInsertTrigger(SQLiteDatabase db, String triggerName, String type, int type_code, String tableName){
        String sql = "CREATE TRIGGER "+triggerName+" after insert on item"+
                " BEGIN"+
                " UPDATE "+tableName+" set "+type+" = "+type+" + new.account WHERE new.type = "+type_code;
        if(tableName.equals("yearsum")){
            sql = sql+" AND year = new.year;";
        }
        else if(tableName.equals("monthsum")){
            sql = sql+" AND year = new.year AND month = new.month;";
        }
        else if(tableName.equals("daysum")){
            sql = sql+" AND year = new.year AND month = new.month AND day = new.day;";
        }
        sql = sql + " END";
        db.execSQL(sql);
    }

    private void createDeleteTrigger(SQLiteDatabase db, String triggerName, String type, int type_code, String tableName){
        String sql = "CREATE TRIGGER "+triggerName+" AFTER DELETE on item"+
                " BEGIN"+
                " UPDATE "+tableName+" SET "+type+" = "+type+" - old.account WHERE old.type = "+type_code;
        if(tableName.equals("yearsum")){
            sql = sql+" AND year = old.year;";
        }
        else if(tableName.equals("monthsum")){
            sql = sql+" AND year = old.year AND month = old.month;";
        }
        else if(tableName.equals("daysum")){
            sql = sql+" AND year = old.year AND month = old.month AND day = old.day;";
        }
        sql = sql + " END";
        db.execSQL(sql);
    }
    private void createUpdateTrigger(SQLiteDatabase db, String triggerName, String type, int type_code, String tableName){
        String sql = "CREATE TRIGGER "+triggerName+" AFTER UPDATE ON item"+
                " BEGIN"+
                " UPDATE "+tableName+" set "+type+" = "+type+" +new.account-old.account WHERE new.type = "+type_code;
        if(tableName.equals("yearsum")){
            sql = sql+" AND year = new.year;";
        }
        else if(tableName.equals("monthsum")){
            sql = sql+" AND year = new.year AND month = new.month;";
        }
        else if(tableName.equals("daysum")){
            sql = sql+" AND year = new.year AND month = new.month AND day = new.day;";
        }
        sql = sql +" END";
        db.execSQL(sql);
    }
}
