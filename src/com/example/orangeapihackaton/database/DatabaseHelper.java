package com.example.orangeapihackaton.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.orangeapihackaton.model.Task;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	static int DATABASE_VERSION = 1;
	static String DATABASE_NAME = "to_do_database.db";
	
	static String TABLE_NAME = "Task";
	public static String TEXT_COLUMN_NAME = "Text";
	//static String RESULTS_COLUMN_POINTS = "Points";
	public static String TEXT_COLUMN_ID = "_id";
	public static String DATE_COLUMN_NAME = "Date";
	
	String TAG = DatabaseHelper.class.getCanonicalName();

	public DatabaseHelper(Context context) {
	
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(TAG, "DatabaseHelper contructor");
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d(TAG,"onCreate");
	database.execSQL("CREATE TABLE " + TABLE_NAME  +
	                 "( " + TEXT_COLUMN_ID  + " integer primary key, " +  DATE_COLUMN_NAME + " date, " +
	                 TEXT_COLUMN_NAME + " text)");
	
	}

	//on upgrade will be invoked if i change version of db in consturctor
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		Log.d(TAG,"onUpgrade");
		
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
		onCreate(database);
		
	}
	
	public void saveRecord(String text){
		Log.d(TAG,"saveRecord : " + text );
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TEXT_COLUMN_NAME, text);
		contentValues.put(DATE_COLUMN_NAME, new Date().toString());
	
		
		database.insert(TABLE_NAME , null, contentValues );
		
	}
	
	public List<Task> getAllRecors(){
		SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DATE_COLUMN_NAME + " DESC", null);

        List<Task> tasks = new LinkedList<Task>();

        if(cursor.moveToFirst()){

            do{
                Task task = new Task();
                Integer id = cursor.getInt(0);
                String date = cursor.getString(1);
                String text = cursor.getString(2);
                task.setId(id);
                task.setDateOfRecord(new Date(date));
                task.setText(text);
                tasks.add(task);
                Log.d(TAG, " " + text + " " + date + " " );
            }while(cursor.moveToNext());

        }

        cursor.close();

         return tasks;
	}

    public void bogusDatabase() {

        Log.d(TAG,"inserting bogus data");
        List<String> texts = new LinkedList<String>();
        texts.add("i go to gallery on 20:00");
        texts.add("i have meeting at krakowska street at 15:00");

        SQLiteDatabase database = this.getWritableDatabase();

        for(String text : texts){
        ContentValues contentValues = new ContentValues();

        contentValues.put(TEXT_COLUMN_NAME, text);
        contentValues.put(DATE_COLUMN_NAME, new Date().toString());


        database.insert(TABLE_NAME , null, contentValues );}

        }
}
