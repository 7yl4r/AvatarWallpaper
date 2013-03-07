package edu.usf.eng.pie.avatars4change.receivers;

import ly.count.android.api.Countly;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.usf.eng.pie.avatars4change.userData.*;


public class myRunsDataCollectorReceiver extends BroadcastReceiver {
	private static int[] levels = new int[5];
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    
		//do something based on the intent's action
		
    	//String url = context.getStringExtra("HOLA");
    	String url = intent.getExtras().getString("HOLA");

    	int sum = 0;
    	//push buffer to make room for new value
    	for (int i = 0 ; i < levels.length-1 ; i++){
    		levels[i] = levels[i+1];
    		sum += levels[i];
    	}
    	levels[levels.length-1] = Integer.valueOf(url) ;
    	sum += levels[levels.length-1];

    	int avgLevel = Math.round(sum/levels.length);
    	userData.currentActivityLevel = avgLevel;
    	
    	if(url.equals("0"))
    		{url="Standing";}
    	if(url.equals("1"))
    		{url="Walking";}
    	if(url.equals("2"))
    		{url="Running";}
    	//Toast.makeText(context, url, Toast.LENGTH_SHORT).show();	

    	Log.v("Llegando", Integer.toString(levels[levels.length-1]) + "->" + url + "5sec avg: " + avgLevel);
    	
    	userData.currentActivity = url;
    	if(avgLevel != 0){	//do not send zero activity notices to countly (is this a good choice?)
			Log.v("MirrorMe Countly Event","queuing event physicalAcitivtyLevel = " + Integer.toString(avgLevel*5));
			Countly.sharedInstance().recordEvent(userData.USERID, avgLevel);
    	}
	}
}
