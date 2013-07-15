package edu.usf.eng.pie.avatars4change.dataInterface;

import java.util.HashMap;

import android.os.Handler;

import ly.count.android.api.Countly;

public class countlyInterface {
	private static Handler delayHandle;
	
	// time between data updates
	private static final long DELAY_TIME = 10000; //TODO: this should be a setting?
	
	private static boolean continuePosting = true;

	public static void startSendingData(){
		continuePosting = true;
		delayHandle = new Handler();
		scheduleDataPost();
	}
	
	public static void resumeSendingData(){
		continuePosting = true;
		scheduleDataPost();
	}
	
	public static void stopSendingData(){
		continuePosting = false;
	}
	
	private static void scheduleDataPost(){
		delayHandle.postDelayed(new Runnable() {
			@Override
			public void run() {
				 postCountlyData();
				 if (continuePosting){
					 scheduleDataPost();
				 }
			}
		},DELAY_TIME);
	}
	
	private static void postCountlyData(){
		if(userData.recentAvg != 0){	//do not send zero activity notices to countly (is this a good choice?)
			//Log.v(TAG,"queuing event physicalAcitivtyLevel = " + Float.toString(avgLevel));
			HashMap<String, String> segmentation = new HashMap<String,String>();
			segmentation.put("UID",userData.USERID);
			Countly.sharedInstance().recordEvent("physicalActivity",segmentation,1, userData.recentAvg);
		}
	}
	
}
