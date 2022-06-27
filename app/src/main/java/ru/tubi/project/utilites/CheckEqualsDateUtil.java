package ru.tubi.project.utilites;

import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.tubi.project.activity.ActivityProduct;

public class CheckEqualsDateUtil {
    private boolean flag=false;

    //проверить даты одинаковы?
    public boolean check(long millis_1, long millis_2){
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.setTimeInMillis(millis_1);
        int day_1 = calendar.get(Calendar.DAY_OF_MONTH);
        int month_1 = calendar.get(Calendar.MONTH);
        int year_1 = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(millis_2);
        int day_2 = calendar.get(Calendar.DAY_OF_MONTH);
        int month_2 = calendar.get(Calendar.MONTH);
        int year_2 = calendar.get(Calendar.YEAR);

        if(day_1 == day_2 && month_1 == month_2 && year_1 == year_2){
            flag = true;
        }else{
            flag = false;
        }
        return flag;
    }


}
