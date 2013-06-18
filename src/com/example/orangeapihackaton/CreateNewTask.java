package com.example.orangeapihackaton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import com.example.orangeapihackaton.analysis.alchemyUtils.AlchemyAnalyzer;
import com.example.orangeapihackaton.analysis.alchemyUtils.DestinationAlchemyAnalyzer;
import com.example.orangeapihackaton.analysis.customUtils.Analyzer;
import com.example.orangeapihackaton.analysis.customUtils.DestinationLocationAnalyzer;
import com.example.orangeapihackaton.database.DatabaseHelper;

public class CreateNewTask extends Activity {

    DatabaseHelper databaseHelper;
    String TAG = CreateNewTask.class.getCanonicalName();
    Analyzer analyzer;
    AlchemyAnalyzer alchemyAnalyzer;
    EditText viewToDo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_task);
        databaseHelper = new DatabaseHelper(this);
        analyzer = new DestinationLocationAnalyzer();
        alchemyAnalyzer = new DestinationAlchemyAnalyzer();
        viewToDo = (EditText)findViewById(R.id.todoText);

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

        String toDoText = viewToDo.getText().toString();
        System.out.println("viewToDo : " + toDoText);

      //  Log.d(TAG, "adding : " + viewToDo.getText().toString());
        //String todoText = "i should go to School at 10:00";//viewToDo.getText().toString();

        if(!(toDoText.length()==0 || toDoText.equals("empty") ))
              databaseHelper.saveRecord(toDoText);


      //  analyzer.analyze(toDoText);
        System.out.println("after analuzing get destination : " + alchemyAnalyzer.analyze(toDoText) );
		
	}

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseHelper.close();
    }


}
