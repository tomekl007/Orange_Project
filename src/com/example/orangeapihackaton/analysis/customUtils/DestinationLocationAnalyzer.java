package com.example.orangeapihackaton.analysis.customUtils;

import com.example.orangeapihackaton.model.Destination;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/12/13
 * Time: 4:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class DestinationLocationAnalyzer implements Analyzer {

    private static List<String> keywords = new LinkedList<String>();

    public DestinationLocationAnalyzer(){
        keywords.add("at");
        keywords.add("on");


    }

    //if text starts from Capital letter that means it is destination place
    @Override
    public void analyze(String text) {
      //List<String> tokens = new LinkedList<String>();
        String space = " ";
        String[] tokens = text.split(space);

        boolean nextHour = false;
        Destination destination = new Destination();
        for(String t : tokens){
            if(keywords.contains(t)){
                 nextHour = true;
            }
            else if(nextHour){
                Calendar calendar = new GregorianCalendar();
                String[] hourAndMinute = t.split(":");
                Integer hour = Integer.valueOf(hourAndMinute[0]);
                Integer minute = Integer.valueOf(hourAndMinute[1]);
                 calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                destination.setDestinationTime(calendar);

            }

            if(Character.isUpperCase(t.charAt(0))){
                destination.setPlace(t);
            }


        }
        System.out.println("after analyzig obj: " + destination);

    }
}
