package com.example.orangeapihackaton.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/12/13
 * Time: 4:50 AM
 * To change this template use File | Settings | File Templates.
 */
//object representing destination place
public class Destination {
    private String place;
    private Calendar destinationTime;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Calendar getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(Calendar destinationTime) {
        this.destinationTime = destinationTime;
    }

    @Override
    public String toString() {
        return destinationTime + " place : "  + place;
    }

    public static Destination destinationFactory(String place, String hourString){
         Destination destination = new Destination();
        destination.setPlace(place);

        Calendar calendar = new GregorianCalendar();
        String[] hourAndMinute = hourString.split(":");
        Integer hour = Integer.valueOf(hourAndMinute[0]);
        Integer minute;
        if(hourAndMinute.length>1){
         minute = Integer.valueOf(hourAndMinute[1]);
        }
        else
         minute = 0;
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        destination.setDestinationTime(calendar);

        return destination;
    }
}

