package com.jnu.pureaccount.data;

//合计类
public class MonthTotalItem extends HomeItem{
    private int year;
    private int month;
    private double total;
    private double income;
    private double expend;

    public MonthTotalItem(double income, double expend){
        this.income = income;
        this.expend = expend;
    }


    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpend() {
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
