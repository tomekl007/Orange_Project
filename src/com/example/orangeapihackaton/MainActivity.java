package com.example.orangeapihackaton;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.example.orangeapihackaton.analysis.luceneUtils.Indexer;
import com.example.orangeapihackaton.analysis.luceneUtils.SimpleIndexer;
import com.example.orangeapihackaton.database.DatabaseHelper;
import com.example.orangeapihackaton.database.ResultList;
import com.example.orangeapihackaton.model.Task;

import java.util.List;

public class MainActivity extends Activity {



    Indexer indexer;
    DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        //Injector injector = Guice.createInjector(new LuceneModule());
      //  System.out.println("-> injector : " + injector);
       // databaseHelper = new DatabaseHelper(this);
        indexer = new SimpleIndexer();//injector.getInstance(Indexer.class);
        System.out.println("init indexer thread for : " + indexer);
        initIndexerThread();
	}

    private void initIndexerThread() {




               // final Intent intent = new Intent(this, MainActivity.class);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Thread bg  = new Thread(new Runnable() {
                //    int interval = 60 * 60 * 24;   //one day
                 //   public void run() {
                      //  while(true){


                         //   try {
                                List<Task> tasks = databaseHelper.getAllRecors();
                                System.out.println("all tasks : " + tasks);
                               databaseHelper.close();
                               indexer.index(tasks);
                               // Thread.currentThread().sleep(interval);
                          //  } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                              //  e.printStackTrace();
                         //   }
                      //  }





                    //}
             //   });
             //   bg.start();




    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void goToCreatingNewTask(View view){
		Intent intent = new Intent(this,CreateNewTask.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); it clear history
  	   	startActivity(intent);
		
	}

    public void goToResultList(View view){
        Intent intent = new Intent(this,ResultList.class);
        startActivity(intent);

    }


}
