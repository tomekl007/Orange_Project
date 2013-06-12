package com.example.orangeapihackaton.model;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/12/13
 * Time: 4:50 AM
 * To change this template use File | Settings | File Templates.
 */
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
}

