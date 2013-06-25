package com.example.orangeapihackaton.analysis.alchemyUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/18/13
 * Time: 3:03 AM
 * To change this template use File | Settings | File Templates.
 */

public interface AlchemyAnalyzer<T> {


    T analyze(String text);
}
