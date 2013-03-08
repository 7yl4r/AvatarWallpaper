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
	
    public static void constant(Avatar theAvatar){
    	//do nothing to update the behavior
    }
    
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
}
