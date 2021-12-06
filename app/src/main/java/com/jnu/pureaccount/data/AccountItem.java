package com.jnu.pureaccount.data;
import android.content.Context;

import com.jnu.pureaccount.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AccountItem extends HomeItem {
    private int icon;
    private int reason;
    private double account;
    private int type; //0支出 1收入
    private Calendar date;
    private String createTime;//条目创建时间，24小时制，格式yyyy-MM-dd HH:mm:ss

    public AccountItem(int reason, double account, int year,int month,int date) {
        this.icon = getIcon(reason);
        this.reason = reason;
        this.account = account;
        this.type = getType();
        this.date = Calendar.getInstance();
        this.date.set(year, month, date);
    }

    public AccountItem(int reason, double account,Calendar calendar, String createTime){
        this.icon = getIcon(reason);
        this.reason = reason;
        this.account = account;
        this.type = getType();
        this.date = calendar;
        this.createTime = createTime;
    }

    public String getTitle(Context context){
        return getTitle(context,this.reason);
    }


    public String getTitle(Context context, int reason){
        switch (reason){
            case 1:
                return context.getString(R.string.food);
            case 2:
                return context.getString(R.string.entertainment);
            case 3:
                return context.getString(R.string.clothes);
            case 4:
                return context.getString(R.string.pets);
            case 5:
                return context.getString(R.string.houserent);
            case 6:
                return context.getString(R.string.medicine);
            case 7:
                return context.getString(R.string.shopping);
            case 8:
                return context.getString(R.string.traffic);
            case 9:
                return context.getString(R.string.tour);
            case 10:
                return context.getString(R.string.study);
            case 11:
                return context.getString(R.string.salary);
            case 12:
                return context.getString(R.string.winning);
            case 13:
                return context.getString(R.string.investment);
            case 14:
                return context.getString(R.string.business);
            default:
                return context.getString(R.string.others);
        }
    }

    public int getIcon(int reason) {
        switch (reason){
            case 1:
                return R.drawable.icon_food;
            case 2:
                return R.drawable.icon_entertainment;
            case 3:
                return R.drawable.icon_clothes;
            case 4:
                return R.drawable.icon_makeup;
            case 5:
                return R.drawable.icon_houserent;
            case 6:
                return R.drawable.icon_medicine;
            case 7:
                return R.drawable.icon_shopping;
            case 8:
                return R.drawable.icon_traffic;
            case 9:
                return R.drawable.icon_tour;
            case 10:
                return R.drawable.icon_study;
            case 11:
                return R.drawable.icon_salary;
            case 12:
                return R.drawable.icon_winning;
            case 13:
                return R.drawable.icon_investment;
            case 14:
                return R.drawable.icon_business;
            default:
                return R.drawable.icon_other;
        }
    }

    public int getType(){
        if(this.reason<=10) return 0;
        else return 1;
    }

    public String getPrintType(){
        if(type==0){
            return "支出";
        }
        else return "收入";
    }


    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }
    public int getReason(){
        return reason;
    }

    public void setReason(int reason){
        this.reason = reason;
    }

    public String getTagDate(){
        //获取用于作标签的年月日字符串
        String date;
        Calendar calendar = this.date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public Calendar getDate(){
        return this.date;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }
}
