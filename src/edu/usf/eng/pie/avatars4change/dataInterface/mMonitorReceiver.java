package edu.usf.eng.pie.avatars4change.dataInterface;

import edu.usf.eng.pie.avatars4change.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
			
			showDebugNotification(context,intent,value);
			
		} else {
			Toast.makeText(context, "application intent not verified\n",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showDebugNotification(Context contx, Intent intnt, double val){
		//send notification to show output:
		//get ref to NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) contx.getSystemService(ns);
			//initiate the notification
		int icon = R.drawable.thumb; //(notification icon)
	
		CharSequence tickerText = "Avatar receiving data from mMonitor...";
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
			//Define notificaiton's message and PendingIntent:
		CharSequence contentTitle = "mDisplay Activity Notifier";
		CharSequence contentText = "mMonitor BroadCast: " + Double.toString(val);
		PendingIntent contentIntent = PendingIntent.getActivity(contx,0, intnt, 0);
		notification.setLatestEventInfo(contx, contentTitle, contentText, contentIntent);
			//Pass notification to manager
		final int HELLO_ID = 1;
		mNotificationManager.notify(HELLO_ID, notification);
		
		/*
		//To add vibration, you must include vibrate access permission
		//vibrate
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
		*/
	}
}
