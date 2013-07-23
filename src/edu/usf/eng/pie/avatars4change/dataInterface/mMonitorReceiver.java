package edu.usf.eng.pie.avatars4change.dataInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
//import edu.usf.eng.pie.avatars4change.R;	//import for the R object???

//for receiving the incoming data
public class mMonitorReceiver extends BroadcastReceiver {
	private static final String TAG = "Avatars4Change mMonitorReceiver";
	
//	public static final String DAILY_STATE_CHANGE_INTENT = "com.MILES.MonitoringApp.DailyStateChange";
	public static final String SEC_LEVEL_BROADCAST_INTENT = "com.MILES.MonitoringApp.secLevelData"; 
	public static final String SEC_LEVEL_BROADCAST_NAME   = SEC_LEVEL_BROADCAST_INTENT;
	
	@Override
	public void onReceive(Context context, Intent intent) {	
		//verify intent is coming from expected application (everything should be moved into this if once working properly)
		if (intent.getAction().equals(SEC_LEVEL_BROADCAST_INTENT)){
			//Toast.makeText(context, "SEC_LEVEL_BROADCAST_INTENT "+SEC_LEVEL_BROADCAST_INTENT+" broadcast received\n",
			//		Toast.LENGTH_SHORT).show();
			;//Log.d(TAG,"SEC_LEVEL_BROADCAST_INTENT "+SEC_LEVEL_BROADCAST_INTENT+" broadcast received");
			
			//read in broadcast value
			Bundle extras = intent.getExtras();
			double value = extras.getDouble(SEC_LEVEL_BROADCAST_NAME);
			;//Log.d(TAG,"broadcast value = "+Double.toString(value));
			
	    	userData.appendValueAndRecalc( context, intent, value );	//TODO: userData values should be changed to floats instead?
	//		showDebugNotification(context,intent,value);
			
		} else {
			Toast.makeText(context, "application intent not verified\n",
					Toast.LENGTH_SHORT).show();
		}
	}
}
