package com.example.orangeapihackaton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import com.example.orangeapihackaton.analysis.customUtils.Analyzer;
import com.example.orangeapihackaton.analysis.customUtils.DestinationLocationAnalyzer;
import com.example.orangeapihackaton.database.DatabaseHelper;

public class CreateNewTask extends Activity {

    DatabaseHelper databaseHelper;
    String TAG = CreateNewTask.class.getCanonicalName();
    Analyzer analyzer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_task);
        databaseHelper = new DatabaseHelper(this);
        analyzer = new DestinationLocationAnalyzer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_task, menu);
		return true;
	}
	
	public void addRecordToDatabase(View view){

        System.out.println("add record to Database");
        //String name=LoadPreferences(USER_NAME);

        EditText viewToDo = (EditText)view.findViewById(R.id.todoTextTwo);
        System.out.println("viewToDo : " + viewToDo);

      //  Log.d(TAG, "adding : " + viewToDo.getText().toString());
        String todoText = "i should go to School at 10:00";//viewToDo.getText().toString();

        if(!(todoText.length()==0 || todoText.equals("empty") ))
              databaseHelper.saveRecord(todoText);


        analyzer.analyze(todoText);
		
	}

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseHelper.close();
    }


}
