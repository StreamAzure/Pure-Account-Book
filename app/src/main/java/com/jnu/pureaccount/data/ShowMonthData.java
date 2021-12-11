package com.jnu.pureaccount.data;

import android.content.Context;
import android.util.Log;

import com.jnu.pureaccount.utils.DataUtils;

/**
 * @描述 一个对象里包含横轴值（日期），纵轴值（支出）
 */
public class ShowMonthData {
    private Context context;
    private int year;
    private int month;
    private int day;
    private double expend;

    public ShowMonthData(Context context, int year, int month, int day){
        DataUtils dataUtils = new DataUtils(context);
        this.year= year;
        this.context = context;
        this.month = month;
        this.day = day;
        this.expend = dataUtils.getDayExpend(year,month,day);
        Log.e("ShowMonthData",this.expend+"");
    }

    public int getMonth() {
        return month;
    }

    public double getExpend() {
        return expend;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }
}
