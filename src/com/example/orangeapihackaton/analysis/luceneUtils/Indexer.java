package com.example.orangeapihackaton.analysis.luceneUtils;

import com.example.orangeapihackaton.model.Task;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/11/13
 * Time: 7:43 AM
 * To change this template use File | Settings | File Templates.
 */

public interface Indexer {
     void index(List<Task> allRecors);
}
