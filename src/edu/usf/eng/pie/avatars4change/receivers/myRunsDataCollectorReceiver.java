package edu.usf.eng.pie.avatars4change.receivers;

import ly.count.android.api.Countly;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import edu.usf.eng.pie.avatars4change.userData.*;

import edu.usf.eng.pie.avatars4change.wallpaper.avatarWallpaper;

public class myRunsDataCollectorReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    
		//do something based on the intent's action
		
    	//String url = context.getStringExtra("HOLA");
    	String url = intent.getExtras().getString("HOLA");
    	int level = Integer.valueOf(url);
    	if(url.equals("0"))
    		{url="Standing";}
    	if(url.equals("1"))
    		{url="Walking";}
    	if(url.equals("2"))
    		{url="Running";}
    	
    	Log.d("Llegando", url);
    	
    	Toast.makeText(context, url, Toast.LENGTH_SHORT).show();		
		
    	userData.currentActivity = url;
    	userData.currentActivityLevel = level;
		Log.v("MirrorMe Countly Event","queuing event physicalAcitivtyLevel = " + Integer.toString(level));
		Countly.sharedInstance().recordEvent(userData.USERID, level);
		
	}
}
