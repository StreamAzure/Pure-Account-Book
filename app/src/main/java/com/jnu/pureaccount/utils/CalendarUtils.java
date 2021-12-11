package com.jnu.pureaccount.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    public Calendar StringToCalender(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public Calendar IntToCalender(int year , int month, int day) throws ParseException {
        String str = IntToTimeString(year,month,day);
        return StringToCalender(str);
    }

    public int[] CalenderToInt(Calendar calendar, int[] intDate){
        intDate[0] = calendar.get(Calendar.YEAR);
        intDate[1] = calendar.get(Calendar.MONTH)+1;
        intDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return intDate;
    }

    public int getNowMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getNowYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public String getNowDateString(){
        //获取当前时间，精确到日
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public String getNowTimeString(){
        //获取当前时间，24小时制，精确到毫秒，格式yyyy-MM-dd HH-mm-ss-SSS
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return format.format(date);
    }

    public String IntToTimeString(int year, int month, int dayOfMonth){
        //转成"yyyy-mm-dd"格式的字符串
        String date,strYear,strMonth,strDay;
        strYear = Integer.toString(year);
        if(month<10){
            strMonth = "0"+month;
        }
        else strMonth = Integer.toString(month);
        if(dayOfMonth<10){
            strDay = "0"+dayOfMonth;
        }
        else strDay = Integer.toString(dayOfMonth);
        date = strYear+"-"+strMonth+"-"+strDay;
        return date;
    }

    public int[] getNowDate(){
        //获取当前日期，返回顺序年月日，月份已修正
        int[] date = new int[5];
        Calendar calendar = Calendar.getInstance();
        date[0] = calendar.get(Calendar.YEAR);
        date[1] = calendar.get(Calendar.MONTH) + 1;
        date[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return date;
    }

    public int[] TimeStringToInt(String str, int[] intDate){
        //"yyyy-mm-dd"格式的字符串转为年月日整型数组
        String[] strDate = str.split("-");
        if(strDate[1].charAt(0)=='0'){
            strDate[1] = strDate[1].charAt(1)+"";
        }
        if(strDate[2].charAt(0)=='0'){
            strDate[2] = strDate[2].charAt(1)+"";
        }
        for(int i=0;i<3;i++){
            intDate[i] = Integer.parseInt(strDate[i]);
        }
        return intDate;
    }

    /**
     * @description 根据年、月获取总天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
