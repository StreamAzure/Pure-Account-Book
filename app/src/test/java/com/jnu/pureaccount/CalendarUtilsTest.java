package com.jnu.pureaccount;

import static org.junit.Assert.assertEquals;

import com.jnu.pureaccount.utils.CalendarUtils;

import org.junit.Test;

public class CalendarUtilsTest {
    CalendarUtils calendarUtils = new CalendarUtils();
    @Test
    public void getDayByYearMonth_isCorrect(){
        assertEquals(31,CalendarUtils.getDaysByYearMonth(2021,12));
        assertEquals(28,CalendarUtils.getDaysByYearMonth(2021,2));
    }
    @Test
    public void IntToTimeString_isCorrect(){
        assertEquals("2021-02-22",calendarUtils.IntToTimeString(2021,2,22));
    }
}
