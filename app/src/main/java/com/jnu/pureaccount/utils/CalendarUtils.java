package com.jnu.pureaccount.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    public Calendar StringToCalender(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = format.parse(strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
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
        //获取当前日期，返回顺序年月日
        int[] date = new int[5];
        Calendar calendar = Calendar.getInstance();
        date[0] = calendar.get(Calendar.YEAR);
        date[1] = calendar.get(Calendar.MONTH);
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
}
