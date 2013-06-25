package com.example.orangeapihackaton.analysis.alchemyUtils;

import com.example.orangeapihackaton.model.Destination;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/18/13
 * Time: 3:03 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * analyzer which analyze input for Destination place, and meeting/arrival Time
 */
public class DestinationAlchemyAnalyzer implements AlchemyAnalyzer<Destination>{

    AlchemyCallFactory callFactory = new AlchemyCallFactory();


    //method analuze input text then extract place, and arrival time
    @Override
    public Destination analyze(String text) {
        Map<OperationsAllowed,String> keywordsFinded = new HashMap<OperationsAllowed, String>();
        keywordsFinded.put(
                OperationsAllowed.CONCEPT,
                callFactory.SendAlchemyCall(OperationsAllowed.CONCEPT, text));

        keywordsFinded.
                put(OperationsAllowed.ENTITY, callFactory.SendAlchemyCall(OperationsAllowed.ENTITY, text));
        keywordsFinded.put(OperationsAllowed.KEYWORD,
                callFactory.SendAlchemyCall(OperationsAllowed.KEYWORD,text));
        keywordsFinded.put(OperationsAllowed.SENTIMENT,
                callFactory.SendAlchemyCall(OperationsAllowed.SENTIMENT, text));
        keywordsFinded.put(OperationsAllowed.TEXT,
                callFactory.SendAlchemyCall(OperationsAllowed.TEXT, text));
        keywordsFinded.put(OperationsAllowed.HOUR,
                callFactory.SendAlchemyCall(OperationsAllowed.HOUR,text));
        System.out.println(keywordsFinded);


         return Destination.destinationFactory(keywordsFinded.get(OperationsAllowed.KEYWORD),
                 keywordsFinded.get(OperationsAllowed.HOUR));
    }


}
