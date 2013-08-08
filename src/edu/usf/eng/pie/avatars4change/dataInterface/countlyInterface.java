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
				 postCountlyPAData();
				 if (continuePosting){
					 scheduleDataPost();
				 }
			}
		},DELAY_TIME);
	}
	
	// posts physical activity data to be sent to countly server
	private static void postCountlyPAData(){
		//Log.v(TAG,"queuing event physicalAcitivtyLevel = " + Float.toString(avgLevel));
		HashMap<String, String> segmentation = new HashMap<String,String>();
		segmentation.put("UID",userData.USERID);
		Countly.sharedInstance().recordEvent("physicalActivity",segmentation,1, userData.recentAvgActivityLevel);
	}
	
	// posts avatar view data to be sent to countly server
	public static void postCountlyViewData(final double amount){
		HashMap<String, String> segmentation = new HashMap<String,String>();
		segmentation.put("UID",userData.USERID);
		Countly.sharedInstance().recordEvent("avatarViewTime",segmentation,1, amount);
	}
	
}
