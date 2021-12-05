package com.jnu.pureaccount.data;

import java.util.Calendar;

//小计类（时间、当天小计）
public class DayTotalItem extends HomeItem {
    private Calendar date;
    private double incomeSubTotal;
    private double expendSubTotal;

    public DayTotalItem(Calendar date, double incomeSubTotal, double expendSubTotal){
        this.date = date;
        this.incomeSubTotal = incomeSubTotal;
        this.expendSubTotal = expendSubTotal;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getIncomeSubTotal() {
        return incomeSubTotal;
    }

    public void setIncomeSubTotal(double incomeSubTotal) {
        this.incomeSubTotal = incomeSubTotal;
    }

    public double getExpendSubTotal() {
        return expendSubTotal;
    }

    public void setExpendSubTotal(double expendSubTotal) {
        this.expendSubTotal = expendSubTotal;
    }

    public String getPrintDate(){
        String date;
        Calendar calendar = getDate();
        int month = calendar.get(Calendar.MONTH) +1;
        int day = calendar.get(Calendar.DATE);
        String sMonth,sDay;
        if(month<10) sMonth = "0"+month;
        else sMonth = month+"";
        if(day<10) sDay = "0"+day;
        else sDay = day+"";
        date = calendar.get(Calendar.YEAR) +"年"+ sMonth+"月"+sDay+"日";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                date = date+" 周一";
                break;
            case 2:
                date = date+" 周二";
                break;
            case 3:
                date = date+" 周三";
                break;
            case 4:
                date = date+" 周四";
                break;
            case 5:
                date = date+" 周五";
                break;
            case 6:
                date = date+" 周六";
                break;
            case 7:
                date = date+" 周日";
                break;
        }
        return date;
    }
}
