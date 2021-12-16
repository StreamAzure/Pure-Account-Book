package com.jnu.pureaccount.data;

import android.content.Context;
import android.util.Log;

import com.jnu.pureaccount.utils.DataUtils;

/**
 * @描述 一个对象里包含横轴值（月份），纵轴值（支出）
 */
public class ShowYearData {
    private Context context;
    private int year;
    private int month;
    private double expend;
    private double income;

    public ShowYearData(Context context, int year, int month){
        DataUtils dataUtils = new DataUtils(context);
        this.year= year;
        this.context = context;
        this.month = month;
        this.expend = dataUtils.getMonthExpend(year,month);
        this.income = dataUtils.getMonthIncome(year,month);
        Log.e("ShowYearData",this.expend+"");
    }

    public int getMonth() {
        return month;
    }

    public double getExpend() {
        return expend;
    }

    public double getIncome() {
        return income;
    }
}
