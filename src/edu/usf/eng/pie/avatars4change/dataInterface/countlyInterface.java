package edu.usf.eng.pie.avatars4change.dataInterface;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import ly.count.android.api.Countly;

public class countlyInterface {
	private static Handler delayHandle;
	private static final String TAG = "dataInterface.countlyInterface";
	
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
	
	//sends image to database
	public static void sendImage(String imgPath){
		//convert image at given path to base64 string
		Bitmap bm = BitmapFactory.decodeFile(imgPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
		byte[] byteArrayImage = baos.toByteArray(); 
		String imgData = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		
		Log.d(TAG+"imgBinary",imgData);
		
		//send data string to db
		HashMap<String, String> segmentation = new HashMap<String,String>();
		segmentation.put("UID",userData.USERID);
		segmentation.put("imgData", "data:image/png;base64,"+imgData);
		Countly.sharedInstance().recordEvent("image",segmentation,1);
	}
}
