package com.jnu.pureaccount.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.db.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
//                Log.e("DataUtils","数据库数据："+accountItem.getTitle(context,reason)+" 金额："+accountItem.getAccount()+" "
//                        +"Date:"+accountItem.getTagDate()+" CreateTime:"+createTime);
                updateData(listTreeMap, accountItem);
                //插入到TreeMap中
            } catch (ParseException e) {
                Log.e("DataUtils",e.getMessage());
            }
        }
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

    //更新一条账目数据
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

    //删除一条账目数据
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

    /**
     * @description 返回给定时间段内各类别的金额统计
     * @param startTime 起始时间，格式“yyyy-MM-dd”
     * @param endTime 结束时间，格式”yyyy-MM-dd“
     * @return Map，key为类别代号，value为对应金额
     */
    public HashMap<Integer,Double> QueryReasonAccount(String startTime, String endTime){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from item where date <= ? and date >= ?",
                new String[]{endTime,startTime});
        cursor.moveToFirst();

        HashMap<Integer,Double> dataReasonAccountCount = new HashMap<>();
        while(!cursor.isAfterLast()){
            int tmpKey = cursor.getInt(1);
            double tmpValue = cursor.getDouble(2);
            if(!dataReasonAccountCount.containsKey(tmpKey)){
                dataReasonAccountCount.put(tmpKey,tmpValue);
            }
            else {
                double v = dataReasonAccountCount.get(tmpKey);
                v += tmpValue;
                dataReasonAccountCount.put(tmpKey,v);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return dataReasonAccountCount;
    }

    /**
     * @description 查询某一日的所有记录
     * @return 当日无记录返回0，否则返回1
     * @throws ParseException
     */
    public boolean QueryDayHistory(ArrayList<HomeItem> dayList, TreeMap<String,List<HomeItem>> listTreeMap, int year,int month, int day) throws ParseException {
        QueryHistory(listTreeMap,"day",year,month,day);
        dayList.clear();
        Iterator<Map.Entry<String,List<HomeItem>>> it = listTreeMap.entrySet().iterator();
        if(it.hasNext()){
            DayTotalItem dayTotalItem = new DayTotalItem(new CalendarUtils().IntToCalender(year,month,day),
                    getDayIncome(year, month, day),getDayExpend(year, month, day));
            dayList.add(dayTotalItem);
        }
        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();//获取该日对应的homeItem列表
            dayList.addAll(homeItems);
        }
        return !dayList.isEmpty();
    }

    /**
     * @description 查询某一月的所有记录
     * @return 当日无记录返回0，否则返回1
     * @throws ParseException
     */
    public boolean QueryMonthHistory(ArrayList<HomeItem> monthList, TreeMap<String,List<HomeItem>> listTreeMap, int year, int month) {
        QueryHistory(listTreeMap,"month",year,month);
        monthList.clear();
        Iterator<Map.Entry<String,List<HomeItem>>> it = listTreeMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();//获取该日对应的homeItem列表
            Calendar calendar = ((AccountItem)homeItems.get(0)).getDate();//从列表中的一项获得该日日期
            DayTotalItem dayTotalItem = new DayTotalItem(calendar,
                    getDayIncome(calendar),getDayExpend(calendar));
            monthList.add(dayTotalItem);//加入当前遍历到的日期Item
            monthList.addAll(homeItems);
        }
        return !monthList.isEmpty();
    }

    public boolean QueryYearHistory(ArrayList<HomeItem> yearList, TreeMap<String,List<HomeItem>> listTreeMap,int year){
        QueryHistory(listTreeMap,"year",year);
        yearList.clear();
        Iterator<Map.Entry<String,List<HomeItem>>> it = listTreeMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();//获取该日对应的homeItem列表
            Calendar calendar = ((AccountItem)homeItems.get(0)).getDate();//从列表中的一项获得该日日期
            DayTotalItem dayTotalItem = new DayTotalItem(calendar,
                    getDayIncome(calendar),getDayExpend(calendar));
            yearList.add(dayTotalItem);//加入当前遍历到的日期Item
            yearList.addAll(homeItems);
        }
        return !yearList.isEmpty();
    }

    /**
     * @param queryType "day", "month", "year"
     * @return 一个TreeMap，按日期从新到旧排序，只包含账目信息
     */
    public boolean QueryHistory(TreeMap<String,List<HomeItem>> listTreeMap, String queryType, int... queryDate){
        listTreeMap.clear();//这里清空了红黑树

        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor;
        switch (queryType){
            case "day":
                cursor = sqLiteDatabase.rawQuery("select * from item where year=? and month=? and day=?",
                        new String[]{queryDate[0]+"",queryDate[1]+"",queryDate[2]+""});
                break;
            case "month":
                cursor = sqLiteDatabase.rawQuery("select * from item where year=? and month=?",
                        new String[]{queryDate[0]+"",queryDate[1]+""});
                break;
            case "year":
                cursor = sqLiteDatabase.rawQuery("select * from item where year=?",
                        new String[]{queryDate[0]+""});
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + queryType);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int reason = cursor.getInt(cursor.getColumnIndexOrThrow("reason"));
            double account = cursor.getDouble(cursor.getColumnIndexOrThrow("account"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String createTime = cursor.getString(cursor.getColumnIndexOrThrow("createTime"));
            try {
                AccountItem accountItem = new AccountItem(reason,account,new CalendarUtils().StringToCalender(date),createTime);
                updateData(listTreeMap,accountItem);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        cursor.close();
        return !(listTreeMap.isEmpty());
    }


    public double getDayExpend(int year, int month, int day){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from daysum where year="+year+" and month="+month+" and day="+day+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        //注意第4列是花费(从0开始)
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(4);
        result.close();
        return val;
    }

    public double getDayExpend(String date){
        int[] intDate = new int[5];
        new CalendarUtils().TimeStringToInt(date, intDate);
        return getDayExpend(intDate[0],intDate[1],intDate[2]);
    }

    public double getDayExpend(Calendar calendar){
        int[] intDate = new int[5];
        new CalendarUtils().CalenderToInt(calendar,intDate);
        return getDayExpend(intDate[0],intDate[1],intDate[2]);
    }

    public double getDayIncome(int year, int month, int day){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from daysum where year="+year+" and month="+month+" and day="+day+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        //注意第3列是收入(从0开始)
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(3);
        result.close();
        return val;
    }

    public double getDayIncome(String date){
        int[] intDate = new int[5];
        new CalendarUtils().TimeStringToInt(date, intDate);
        return getDayIncome(intDate[0],intDate[1],intDate[2]);
    }

    public double getDayIncome(Calendar calendar){
        int[] intDate = new int[5];
        new CalendarUtils().CalenderToInt(calendar,intDate);
        return getDayIncome(intDate[0],intDate[1],intDate[2]);
    }

    public double getMonthIncome(int year, int month){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from monthsum where year="+year+" and month="+month+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(2);
        result.close();
        return val;
    }

    public double getMonthExpend(int year, int month){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from monthsum where year="+year+" and month="+month+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(3);
        result.close();
        return val;
    }

    public double getYearIncome(int year){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from yearsum where year="+year+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(1);
        result.close();
        return val;
    }

    public double getYearExpend(int year){
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context,dbName,null,dbVersion);
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String sql = "select * from yearsum where year="+year+";";
        Cursor result = sqLiteDatabase.rawQuery(sql,new String[]{});
        result.moveToFirst();
        double val = 0;
        if(!result.isAfterLast()) val = result.getDouble(2);
        result.close();
        return val;
    }


}
