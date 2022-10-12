package com.outzone.main.tools;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * Herramienta de gestión de las fechas en el sistema:
 * - formato normalizado en el sistema
 * - convesión entre String e Integer
 * - adecuación de los string requeridos por Room para las consultas
 **/
public class DateManager {
    public static String getActualDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        return dtf.format(LocalDate.now());
    }

    public static int[] getActualDateInt(){
        int [] date = {LocalDate.now().get(ChronoField.YEAR),LocalDate.now().get(ChronoField.MONTH_OF_YEAR),LocalDate.now().get(ChronoField.DAY_OF_MONTH)};
        return date;
    }

    public static String formatDate(int year, int month, int day){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        LocalDate ld = LocalDate.of(year,month,day);
        return dtf.format(ld);
    }

    public static String formatMonthQuery(int year, int month){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("/MM/uuuu");
        LocalDate ld = LocalDate.of(year,month,1);
        return "%" + dtf.format(ld) + "%";
    }

    public static int[] getDateFromString(String date){
        String[] str = date.split("/");
        int[] outDate = {Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2])};
        return outDate;
    }

    public static int[] getNextSaturday(String date){
        String[] str = date.split("/");
        int[] outDate = {Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2])};

        int year = outDate[2];
        int month = outDate[1];
        int day = outDate[0];
        int dow;
        dow = getDayOfWeek(year,month,day);
        while (dow!=7){
            if (month == 12 && day == getNDaysMonth(year,month)) {
                year++;
            } else if (day >= getNDaysMonth(year, month)) {
                month++;
            } else {
                day++;
            }
            dow = getDayOfWeek(year,month,day);
        }
        outDate[0]=day; outDate[1]=month; outDate[2]=year;
        return outDate;
    }

    public static int getNDaysMonth(int year, int month){
        int[] nDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        if ((year-2000)%4==0)
            nDays[1] = 29;
        return nDays[month-1];
    }

    public static int getDayOfWeek(int year, int month, int day){
        int y = (year+(year/4))%7;
        int[] m = {0,3,3,6,1,4,6,2,5,0,3,5};
        int c=6; //1700=4, 1800=2; 1900=0; 2000=6; 2100=4; 2200=2; 2300=0;
        int l;

        if ((year)%4==0 && year%100!=0 || year%400==0)
        {
            if (month == 1 || month == 2)
                l = 1;
            else
                l = 0;
        }else{
            l = 0;
        }

        int res = (y + m[month-1] + c + day - l)%7;
        if (res == 0)
            return 7;
        else
            return res;
    }
}
