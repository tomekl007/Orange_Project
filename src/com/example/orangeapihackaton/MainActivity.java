package com.example.orangeapihackaton;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.example.orangeapihackaton.analysis.luceneUtils.Indexer;
import com.example.orangeapihackaton.database.DatabaseHelper;
import com.example.orangeapihackaton.database.ResultList;
import com.example.orangeapihackaton.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * activity in which user get simple menu with two options for now
 */
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
     //   databaseHelper.bogusDatabase();//for now init with some data
     //   indexer = new SimpleIndexer();//injector.getInstance(Indexer.class);
      //  searcher = new DestinationLocationSearcher();
     //   System.out.println("init indexer thread for : " + indexer);
     //   initIndexerThread();
	}

    private void initIndexerThread() {


        final List<Task> tasks = databaseHelper.getAllRecors();
        System.out.println("all tasks : " + tasks);
        databaseHelper.close();


               // final Intent intent = new Intent(this, MainActivity_2.class);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Thread bg  = new Thread(new Runnable() {
                  //  int interval = 60 * 60 * 24;   //one day
                    public void run() {


                               indexer.index(tasks,getTempDir());
                    }


               });
               bg.start();




    }
    private File getTempDir(){
        File outputDir = this.getApplicationContext().getCacheDir(); // context being the Activity pointer
        File outputFile=null;
        try {
            outputFile = File.createTempFile("index", "lucene", outputDir);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return outputDir;
    }

   /* private File createTempFile(){
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput("lucene_index",
                    MODE_WORLD_READABLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        return fOut;


    }  */

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
