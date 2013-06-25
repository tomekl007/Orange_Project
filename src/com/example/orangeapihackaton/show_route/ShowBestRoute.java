package com.example.orangeapihackaton.show_route;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.orangeapihackaton.R;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/25/13
 * Time: 4:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShowBestRoute extends Activity {



    String routeText ;
    TextView bestRouteText;
    static String TAG = ShowBestRoute.class.getCanonicalName();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_best_route);
        Log.d(TAG,"ShowBestRoute onCreate");

        //getExtra
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                routeText= null;
            } else {
                routeText= extras.getString("route");
            }
        }else{
            routeText = "there is no route ";
            Log.d(TAG,"savedInstanceState was null ");
        }


        bestRouteText = (TextView)findViewById(R.id.stringWithBestRoute);
        bestRouteText.setText(routeText);

    }
}