package com.t3h.item;

import java.util.Calendar;

/**
 * Created by vaio on 11/13/2016.
 */

public class Today {
    public static int dayOfMonth(){
        Calendar calendar  = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static int month(){
        Calendar calendar  = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }
    public static int year(){
        Calendar calendar  = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
    public static int dayOfWeek(){
        Calendar calendar  = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
