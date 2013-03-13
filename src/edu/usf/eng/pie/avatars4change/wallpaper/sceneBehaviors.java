package edu.usf.eng.pie.avatars4change.wallpaper;

import java.util.Calendar;
import java.util.TimeZone;

import edu.usf.eng.pie.avatars4change.avatar.Avatar;

import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

public class sceneBehaviors {
    public static boolean   activeOnEvens       = true;	//active on even days?
    public static long      deltaActivityChange = 5*1000;	//60*60*1000;	//desired time between activity level updates [ms]
	
    //this method gets a behavior from the Avatar's behavior string (which has been set in the settings)
    public static void runBehavior(Avatar theAvatar){
    	if ( theAvatar.behaviorSelectorMethod.equalsIgnoreCase("constant") ){
    		constant(theAvatar);
    	}else if( theAvatar.behaviorSelectorMethod.equalsIgnoreCase("Proteus Effect Study")){
    		proteusStudy(theAvatar);
    	}else if( theAvatar.behaviorSelectorMethod.equalsIgnoreCase("IEEE VR demo")){
    		VRDemo(theAvatar);
    	}else{
    		debug(theAvatar);	//default method
    	}
    }
    
    // avatar behavior does not change; it stays constant as it has been set
    public static void constant(Avatar theAvatar){
    	//do nothing to update the behavior, it stays your choice
    }
    
    // avatar behavior designed for use in the Proteus Effect study
	public static void proteusStudy(Avatar theAvatar){
		//check for enough time to change animation
    	//TODO: change this next if issue#5 persists
		long now = SystemClock.elapsedRealtime();		//TODO: ensure that this works even if phone switched off. 
        if((now - theAvatar.lastActivityChange) > deltaActivityChange){		//if time elapsed > desired time
        	//if past bedTime and before wakeTime, sleep
            int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            Log.v("Avatars4Change Avatar sleep clock", "current hour:" + currentHour);
            if(currentHour >= theAvatar.bedTime || currentHour < theAvatar.wakeTime){
            	//draw sleeping
            	theAvatar.setActivityLevel("sleeping");
            } else {	//awake
            	int today = Time.getJulianDay(System.currentTimeMillis(), (long) (TimeZone.getDefault().getRawOffset()/1000.0) ); 	//(new Time()).toMillis(false)
            	Log.v("Avatars4Change day calculator","time:"+System.currentTimeMillis()+"\ttimezone:"+TimeZone.getDefault().getRawOffset()+"\ttoday:"+today);
            	//set active or passive, depending on even or odd julian day
            	if(today%2 == 0){	//if today is even
            		if(activeOnEvens){
            			theAvatar.setActivityLevel("active");
            		}else{
            			theAvatar.setActivityLevel("passive");
            		}
            	}else{	//today is odd
            		if(!activeOnEvens){	//if active on odd days
            			theAvatar.setActivityLevel("active");
            		}else{
            			theAvatar.setActivityLevel("passive");
            		}
            	}
            }
        	//avatar changes activity 
        	theAvatar.randomActivity(theAvatar.getActivityLevel());
       	 	theAvatar.lastActivityChange = now;
        }
	}

	// avatar behavior cycles through all behaviors in order on a short interval
	public static void debug(Avatar theAvatar){
		//TODO make this happen...
		constant(theAvatar);
	}

	// avatar shows sedentary behavior for sitting, slow active behavior for walking, fast active behavior for running
	public static void VRDemo(Avatar theAvatar){
		constant(theAvatar);
	}
}
