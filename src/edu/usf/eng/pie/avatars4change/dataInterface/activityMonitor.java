package edu.usf.eng.pie.avatars4change.dataInterface;

import edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.Globals;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class activityMonitor {
	private static final String TAG = "dataInterface.activityMonitor";
	private static final String[] supportedMonitors = {"built-in","mMonitor"};	//list of supported activity monitors
	
    private static String currentActivityMonitor = "none"; //name of current activity monitor method used
	private static myRunsDataCollectorReceiver builtInReceiver  = new myRunsDataCollectorReceiver();
	private static mMonitorReceiver            mMonitorReceiver = new mMonitorReceiver();
	
	private static final IntentFilter mMonitorIntentFilter = new IntentFilter("com.MILES.MonitoringApp.secLevelData");
	private static final IntentFilter builtInIntentFilter  = new IntentFilter("SOME_ACTION"); // TODO: change "SOME_ACTION"

	public static String getActivityMonitor(){
		return currentActivityMonitor;
	}
	
	public static void onClose(Context contx){
	// this method should be called once the monitor is no longer needed
	// to do any needed cleanup.
		tearDownActivityMonitor(contx);
	}
	
	public static void resetActivityMonitor(Context contx){
	// restart the activity monitor to fix any hiccups
		final String method = getActivityMonitor();
		tearDownActivityMonitor(contx);
		startupActivityMonitor(contx, method);
		Log.i(TAG,"activity monitor '"+method+"' reset");
	}
		
	public static void setActivityMonitor(Context contx, String newMethod){
	// used to change the activity monitor method
		for (int i = 0; i<supportedMonitors.length; i++){
			if (supportedMonitors[i].equals(newMethod)){	//check for acceptable newMethod
				if (newMethod.equals(currentActivityMonitor)){	//check for method already running
					Log.i(TAG,"activity monitor '"+newMethod+"' already running");
					return;
				} else {
					tearDownActivityMonitor(contx);
					startupActivityMonitor(contx, newMethod);
					return;
				}
			}
		} // else: (exit of for loop means that newMethod is unrecognized)
		Log.e(TAG,"unrecognized behaviorSelector '"+newMethod+"'! using default 'built-in'");
		currentActivityMonitor = "built-in";
		return;
	}
	
	private static void startupActivityMonitor(Context contx, String newMethod){
		if (newMethod.equals("mMonitor")){
			//TODO: request startService on mMonitor, 
			contx.registerReceiver(mMonitorReceiver, mMonitorIntentFilter); // startup mMonitor reciever
			//TODO: wait for some mMonitor data coming in, print error and/or switch to built-in if problem
			Log.i(TAG,"assuming that mMonitor is running correctly");
		} else if(newMethod.equals("built-in")){
			PAcollectorSetup(contx);	//start up the service
			contx.registerReceiver(builtInReceiver, builtInIntentFilter); // start up the receiver 
		} else {
			Log.e(TAG,"activity monitor method '"+newMethod+"' not recognized!, using built-in");
			startupActivityMonitor(contx,"built-in");
			return;
		}
		currentActivityMonitor = newMethod;
		return;
	}
	
	//stops the currently active monitor
	private static void tearDownActivityMonitor(Context ctx){
		if (currentActivityMonitor.equals("mMonitor")){
			try{
				ctx.unregisterReceiver(mMonitorReceiver);
			} catch (IllegalArgumentException e){
				Log.i(TAG,"error unregistering receiver: "+e.getMessage());
			}
			//TODO: release mMonitor service using ctx.stopService()
		} else if(currentActivityMonitor.equals("built-in")){
			try{
				ctx.unregisterReceiver(builtInReceiver);
			} catch (IllegalArgumentException e){
				Log.i(TAG,"error unregistering receiver: "+e.getMessage());
			}
			PAcollectorStop(ctx);// stop the PA monitor service
		} else {
			Log.e(TAG,"active ctivity monitor method '"+currentActivityMonitor+"' not recognized! cannot stop it!");
			return;	//this skips the last line
		}
		currentActivityMonitor = "None";
	}
	
	//sets up the built-in physical activity collector service
	private static void PAcollectorSetup(Context ctx){
 		Log.v(TAG, "starting PAcollector SensorService");
 		ctx.startService(getPAcollectorServiceIntent(ctx)); 
	}
	//stops the built-in physical activity collector service
	private static void PAcollectorStop(Context ctx){
		Log.v(TAG, "stopping PAcollector SensorService");
 		ctx.stopService(getPAcollectorServiceIntent(ctx)); 
	}
	private static Intent getPAcollectorServiceIntent(Context ctx){
	    final String[] mLabels       = {"still", "walking", "running"};
	    Intent mServiceIntent = new Intent(ctx, edu.usf.eng.pie.avatars4change.myrunsdatacollectorlite.ServiceSensors.class);
 		int activityId = Globals.SERVICE_TASK_TYPE_CLASSIFY;	//TODO: what?
 		String label = mLabels[activityId];
 		Bundle extras = new Bundle();
 		extras.putString("label", label);
 		extras.putString("type", "collecting");
 		mServiceIntent.putExtras(extras);
 		return mServiceIntent;
	}
}
