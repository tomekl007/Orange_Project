package com.example.orangeapihackaton.show_route;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/25/13
 * Time: 4:13 AM
 * To change this template use File | Settings | File Templates.
 */

public class AlarmManagerUtils {
    Context context ;

    public AlarmManagerUtils(Context context){
        this.context = context;
    }


    /**
     *
     *

     * @param hourOffset   hour offset from "now"
     * @param minuteOffset minut offset from "now"
     * @param bestRouteDescription string contating best route description
     */
    public void setAlarmAtSpecyficHour(int hourOffset, int minuteOffset, String bestRouteDescription){


        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context, ShowBestRoute.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("route",bestRouteDescription);

        PendingIntent pi= PendingIntent.getActivity(context, 0, i, 0);

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());


        time.add(Calendar.HOUR, hourOffset);
        time.add(Calendar.MINUTE, minuteOffset);


        System.out.println("setting alarm manager");
        alarmManager.set(AlarmManager.RTC_WAKEUP,time.getTimeInMillis(),pi);



    }

}
