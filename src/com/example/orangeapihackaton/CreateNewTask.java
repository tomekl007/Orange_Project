package com.example.orangeapihackaton;

import Data.BusStop;
import Data.Line;
import Route.Algorithm;
import Threads.BusStopDataThread;
import Threads.LineDataThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import com.example.orangeapihackaton.analysis.alchemyUtils.AlchemyAnalyzer;
import com.example.orangeapihackaton.analysis.alchemyUtils.DestinationAlchemyAnalyzer;
import com.example.orangeapihackaton.analysis.customUtils.Analyzer;
import com.example.orangeapihackaton.analysis.customUtils.DestinationLocationAnalyzer;
import com.example.orangeapihackaton.database.DatabaseHelper;
import com.example.orangeapihackaton.database.ResultList;
import com.example.orangeapihackaton.model.Destination;
import com.example.orangeapihackaton.show_route.AlarmManagerUtils;

/**
 * acitivity responsible for creating new task, adding it to database,
 * and fires up analyze event
 */
public class CreateNewTask extends Activity {

    DatabaseHelper databaseHelper;

    Analyzer analyzer;
    AlchemyAnalyzer alchemyAnalyzer;
    EditText viewToDo;
    AlarmManagerUtils alarmManagerUtils;
    static String TAG = CreateNewTask.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_task);
        databaseHelper = new DatabaseHelper(this);
        analyzer = new DestinationLocationAnalyzer();
        alchemyAnalyzer = new DestinationAlchemyAnalyzer();
        viewToDo = (EditText)findViewById(R.id.todoText);
        alarmManagerUtils = new AlarmManagerUtils(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_task, menu);
		return true;
	}

    /**
     * after adding record to database task is analyzed by AlechemyApi
     * then best route is find and alarm with activity presenting this best
     * route is scheduled
     * @param view
     */
	public void addRecordToDatabase(View view){

        System.out.println("add record to Database");
        //String name=LoadPreferences(USER_NAME);

        String toDoText = viewToDo.getText().toString();
        System.out.println("viewToDo : " + toDoText);

      //  Log.d(TAG, "adding : " + viewToDo.getText().toString());
        //String todoText = "i should go to School at 10:00";//viewToDo.getText().toString();

        if(!(toDoText.length()==0 || toDoText.equals("empty") ))
              databaseHelper.saveRecord(toDoText);


      //  analyzer.analyze(toDoText);
        Destination destination = (Destination) alchemyAnalyzer.analyze(toDoText);
        System.out.println("after analuzing get destination : " + destination);
        //tutaj wywolanie api wyszukujacego optymalna trase lineFInder(destination)
        findBestRoute(destination);

        alarmManagerUtils.setAlarmAtSpecyficHour(0, 1 , "there is no best route for now");

        Intent intent = new Intent(this, ResultList.class);
        startActivity(intent);

	}

    private void findBestRoute(Destination destination) {

        Log.d(TAG, "findBestROute");



        BusStop.clearStaticData();
        Line.clearStaticData();

        BusStop.readIDsData();
        LineDataThread.startThreads(10);
        BusStopDataThread.startThreads(5);
        Algorithm.test();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseHelper.close();
    }


}
