package com.example.orangeapihackaton.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.orangeapihackaton.R;

/**
 * adapter used to presenting history of tasks in
 * ListView component
 */
public class HistoryAdapter extends CursorAdapter {
    public static String TEXT_COLUMN_NAME = "Text";

    @SuppressWarnings("deprecation")
    public HistoryAdapter(Context context, Cursor c) {
        super(context, c);

    }


    @Override//to populate the view with data
    public void bindView(View view, Context arg1, Cursor cursor) {
        TextView nameText = (TextView) view.findViewById(R.id.textRecord);
        nameText.setText(cursor.getString(cursor.getColumnIndex(TEXT_COLUMN_NAME)));




    }

    @Override//to create the view
    public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_result_list, parent, false);

        return view;
    }


}