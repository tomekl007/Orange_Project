package com.example.orangeapihackaton.tests;


import android.content.Context;
import android.test.RenamingDelegatingContext;
import android.test.ServiceTestCase;
import com.example.orangeapihackaton.database.DatabaseHelper;
import com.example.orangeapihackaton.model.Task;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/11/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseTest {

    public DatabaseHelper databaseHelper;


    @Before
    public void setUp() {
        RenamingDelegatingContext context
                = new RenamingDelegatingContext(getTestContext(), "test_");

        databaseHelper = new DatabaseHelper(context);
        System.out.println("@Before - setUp");
    }

    private Context getTestContext()
    {
        try
        {
            Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
            return (Context) getTestContext.invoke(this);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }



    @Test
    void testAddingToDatabase(){
        String exampleRecord = "record1";
        String expectedRecord = "";
         databaseHelper.saveRecord(exampleRecord);

        List<Task> tasks = databaseHelper.getAllRecors();

        Boolean doContain = tasks.contains(exampleRecord);


        assertEquals(new Boolean("true"), doContain);

    }
}
