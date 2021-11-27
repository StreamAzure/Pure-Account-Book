package com.jnu.pureaccount.data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountItem extends HomeItem {
    private int icon;
    private int reason;
    private int account;
    private boolean type; //0支出 1收入
    private Calendar date;


    public AccountItem(int icon, int reason, int account, boolean type, int year,int month,int date) {
        this.icon = icon;
        this.reason = reason;
        this.account = account;
        this.type = type;
        this.date = Calendar.getInstance();
        this.date.set(year, month, date);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
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
}
