package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {
    public static String getCurrentDate(int dateAddValue){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date Date1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date1);
        calendar.add(Calendar.DATE, dateAddValue);
        String requiredDate = df.format(calendar.getTime()).toString();
        return requiredDate;
    }
    public static String getCurrentDate(int dateAddValue, String DateFormat){
        DateFormat df = new SimpleDateFormat(DateFormat);
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Date Date1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date1);
        calendar.add(Calendar.DATE, dateAddValue);
        String requiredDate = df.format(calendar.getTime()).toString();
        return requiredDate;
    }

    public static String getCurrentTime(int hoursAddValue, int minutesAddValue) {
        DateFormat tf = new SimpleDateFormat("HH:mm");
        tf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hoursAddValue);
        calendar.add(Calendar.MINUTE, minutesAddValue);
        Date newTime = calendar.getTime();
        String requiredTime = tf.format(newTime).toString();
        return requiredTime;
    }
    public static String getCurrentMonth (){
        Calendar calendar = Calendar.getInstance();
        String[] monthNames = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
        String month = monthNames[calendar.get(Calendar.MONTH)];
        return month;
    }

    public static int getCurrentYear (){
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        return  year;
    }
}
