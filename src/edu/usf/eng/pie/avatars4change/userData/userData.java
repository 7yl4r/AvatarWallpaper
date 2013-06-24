package edu.usf.eng.pie.avatars4change.userData;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class userData {
	private static String TAG = "userData";
	//user info:
    public static String USERID = "defaultUID";
    
    //user context:
    public static String currentActivity = "???";
    public static float    currentActivityLevel = 0;
    public static double[] FFT = new double[65];
    
    private static Handler delayHandle;
    
    public void onCreate(Bundle oldState){
    	//super.onCreate(oldState);
    	delayHandle = new Handler();
    }
    
    public static String getFileDir(Context context){
    	//hang here until storage is ready
    	waitForStorageIsReady(context);
    	try{
	    	//return storage location
	    	String result = context.getExternalFilesDir(null).toString()+"/";	//TODO: implement this
	    	Log.v(TAG,"fDir="+result);
	    	return result;
    	} catch (NullPointerException e){
    		Log.e(TAG,"sdCard found, but cannot get internal storage for unknown reason");
    		try {
    			showSDcardError(context);
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		return getFileDir(context);
    	}

    }
    
    private static void showSDcardError(Context context){
    	Log.e(TAG,"attempt to access SDcard when no SDcard present!");
		Toast ERRmessage = Toast.makeText(context, "Avatar App cannot access sdCard!", Toast.LENGTH_SHORT);
		ERRmessage.show();
    }
    
    //schedules delayed run
    private static void waitForStorageIsReady(final Context context){
    	if(storageReady()){
    		return;
    	} else {
    		showSDcardError(context);
    		try{
        		delayHandle.postDelayed(new Runnable() {
    				@Override
    				public void run() {
    					 waitForStorageIsReady(context);
    				}
    			},500);
    		}catch(NullPointerException e){
    			Log.e(TAG,"handler for noSDcardDelay not found; creating new");
    			delayHandle = new Handler();
    			waitForStorageIsReady(context); //restart the function
    		}
    	}
    }
    
    //returns true if external storage is readable and writable
    // further utility is availble using commented out items
    private static boolean storageReady(){
    	//boolean mExternalStorageAvailable = false;
    	//boolean mExternalStorageWriteable = false;
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	//    mExternalStorageAvailable = mExternalStorageWriteable = true;
    	    return true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	//    mExternalStorageAvailable = true;
    	//    mExternalStorageWriteable = false;
    	    return false;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	//    mExternalStorageAvailable = mExternalStorageWriteable = false;
    	    return false;
    	}
    }
}
