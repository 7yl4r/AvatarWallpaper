package edu.usf.eng.pie.avatars4change.dataInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class myRunsDataCollectorReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {	
		// get the value
    	//String url = context.getStringExtra("HOLA");
    	String url = intent.getExtras().getString("HOLA");
    	
    	// add the numeric value to userData
    	userData.appendValueAndRecalc(context,intent,Integer.valueOf(url));

    	// set the name of current activity based on classification
    	if(url.equals("0"))
    		{url="Standing";}
    	if(url.equals("1"))
    		{url="Walking";}
    	if(url.equals("2"))
    		{url="Running";}
    	userData.setCurrentActivityName(url);

    	//extra debug info:
//    	Log.d("myRunsDataCollectorReceiver", Integer.toString(userData.recentActivityLevels[userData.recentActivityLevels.length-1]) + 
//    	      "->" + url + "; past " + Integer.toString(userData.recentActivityLevels.length) + 
//    	      "sample avg: " + userData.recentAvgActivityLevel);
	}
}
