package edu.usf.eng.pie.avatars4change.userData;

import edu.usf.eng.pie.avatars4change.wallpaper.avatarWallpaper;
import android.os.Environment;
import android.util.Log;

public class userData {
	private static String TAG = "userData";
	//user info:
    public static String USERID = "defaultUID";
    
    //user context:
    public static String currentActivity = "???";
    public static float    currentActivityLevel = 0;
    public static double[] FFT = new double[65];
    
    public static String getFileDir(){
    	Boolean SDpresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    	if(SDpresent){
    		return  avatarWallpaper.mContext.getExternalFilesDir(null).toString()+"/MirrorMe/";	//TODO: implement this
    	}else{
    		Log.e(TAG,"attempt to access SDcard when no SDcard present!");
    		return null;
    	}
    }
}
