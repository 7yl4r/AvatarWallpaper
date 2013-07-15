package edu.usf.eng.pie.avatars4change.mMonitorInterface;

import edu.usf.eng.pie.avatars4change.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
//import edu.usf.eng.pie.avatars4change.R;	//import for the R object???

//for receiving the incoming data
public class mMonitorReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {	
		
		//verify intent is coming from expected application (everything should be moved into this if once working properly)
		if(intent.getAction().equals("DAILY_STATE_CHANGE_INTENT")){		//perhaps this is not the proper action to listen for?
			Toast.makeText(context, "DAILY_STATE_CHANGE_INTENT recieved\n",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "application intent not verified\n",
					Toast.LENGTH_LONG).show();
		}
		
		//read in broadcast value
		Bundle extras = intent.getExtras();
		int value = 0; 
		String valString = "ERR: valString Empty";
		//string to int
		try {
			valString = extras.getString("DailyStateChangeValue");		//this does not work; string returned is "null", parse error
		    //valString = extras.getString("DAILY_STATE_CHANGE_INTENT");	//this does not work either, same as above
			value = Integer.parseInt(valString);
		} catch(NumberFormatException nfe) {	
		   System.out.println("Could not parse " + nfe);
		}
		
		//value = extras.getInt("DailyStateChangeValue");		//this does not work, value returned is '0'
		//value = extras.getInt("DAILY_STATE_CHANGE_INTENT");		//this does not work either, same as above
		
				
		//send notification to show output:
			//get ref to NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
			//initiate the notification
		int icon = R.drawable.thumb; //(notification icon)

		CharSequence tickerText = "mMonitor's TickerText";
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
			//Define notificaiton's message and PendingIntent:
		CharSequence contentTitle = "mDisplay Activity Notifier";
		CharSequence contentText = "mMonitor BroadCast: " + valString + "=" + value;
		PendingIntent contentIntent = PendingIntent.getActivity(context,0, intent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
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
