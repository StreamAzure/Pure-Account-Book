package com.jnu.pureaccount.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.db.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DataUtils {
    private Context context;
    private String dbName;
    private Integer dbVersion;

    public DataUtils(Context context){
        this.context = context;
        this.dbName = "PureAccount"; //数据库名写死在这了
        dbVersion = 1;
    }

    public void loadItemData(TreeMap<String,List<HomeItem>> listTreeMap){
        //从数据库中加载全部条目
        listTreeMap.clear();
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase(); //只读
        //只有一张表item，收入和支出都在里面，以"type"列区分
        Cursor cursor = sqLiteDatabase.query("item",new String[]{"createTime","reason","account","type","date","year","month","day"}, null,null,null,null,null);
        while(cursor.moveToNext()){
            int reason = cursor.getInt(cursor.getColumnIndexOrThrow("reason"));
            double account = cursor.getDouble(cursor.getColumnIndexOrThrow("account"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String createTime = cursor.getString(cursor.getColumnIndexOrThrow("createTime"));
            try {
                //new一个AccountItem
                AccountItem accountItem = new AccountItem(reason,account,new CalendarUtils().StringToCalender(date),createTime);
                Log.e("DataUtils","数据库数据："+accountItem.getTitle(context,reason)+" 金额："+accountItem.getAccount()+" "
                        +"Date:"+accountItem.getTagDate()+" CreateTime:"+createTime);
                updateData(listTreeMap, accountItem);
                //插入到TreeMap中
            } catch (ParseException e) {
                Log.e("DataUtils",e.getMessage());
            }
        }
    }

    //插入一条账目数据
    public void InsertItemData(int reason, double account, String date){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        int type = 0;
        if ( reason <= 10 ) type = 0;
        else type = 1;

        int[] tmpTime = new int[5];
        new CalendarUtils().TimeStringToInt(date, tmpTime);

        ContentValues values = new ContentValues();
        values.put("createTime",new CalendarUtils().getNowTimeString());
        values.put("reason", reason);
        values.put("account",account);
        values.put("type",type);
        values.put("date",date);
        values.put("year",tmpTime[0]);
        values.put("month",tmpTime[1]);
        values.put("day",tmpTime[2]);

        sqLiteDatabase.insert("item",null,values);
    }

    public void EditItemData(int reason, double account, String date, String createTime){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        int type = 0;
        if ( reason <= 10 ) type = 0;
        else type = 1;

        int[] tmpTime = new int[5];
        new CalendarUtils().TimeStringToInt(date, tmpTime);

        ContentValues values = new ContentValues();
        values.put("reason", reason);
        values.put("account",account);
        values.put("type",type);
        values.put("date",date);
        values.put("year",tmpTime[0]);
        values.put("month",tmpTime[1]);
        values.put("day",tmpTime[2]);

        sqLiteDatabase.update("item", values, "createTime=?", new String[]{createTime});
        //字符串列表内的内容会依次替换whereClause参数里的问号
    }

    public void DeleteItem(String createTime){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        sqLiteDatabase.delete("item","createTime=?",new String[]{createTime});
    }

    //清空表
    public void DeleteTable(String table){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String sql = "delete from "+table;
        sqLiteDatabase.execSQL(sql);
        sql = "update sqlite_sequence SET seq = 0 where name = " + "'"+table+"'";
        sqLiteDatabase.execSQL(sql);
    }

    private void updateData(TreeMap<String,List<HomeItem>> listTreeMap, AccountItem accountItem){
        List<HomeItem> homeItemList;
        if(listTreeMap.containsKey(accountItem.getTagDate())){
            homeItemList = listTreeMap.get(accountItem.getTagDate());
            homeItemList.add(accountItem);
        }
        else{
            homeItemList = new ArrayList<>();
            homeItemList.add(accountItem);
            listTreeMap.put(accountItem.getTagDate(),homeItemList);
        }
    }

    public void QueryTable(String tableName){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor result=sqLiteDatabase.rawQuery("select * from "+tableName,new String[]{});
        result.moveToFirst();
        while (!result.isAfterLast()) {
            if(tableName.equals("daysum")) {
                Log.e("DataUtils", tableName + "： " + result.getInt(0) + " " + result.getInt(1) + " " +
                        result.getInt(2) + " income:" + result.getDouble(3) + " expend:" + result.getDouble(4));
            }
            else if(tableName.equals("monthsum")){
                Log.e("DataUtils", tableName + "： " + result.getInt(0) + " " + result.getInt(1) + " income:" +
                        result.getDouble(2) + " expend:" + result.getDouble(3));
            }
            else if(tableName.equals("yearsum")){
                Log.e("DataUtils", tableName + "： " + result.getInt(0) + " income:" + result.getDouble(1) + " expend:" +
                        result.getDouble(2) + " ");
            }
            result.moveToNext();
        }
        result.close();
    }
}
