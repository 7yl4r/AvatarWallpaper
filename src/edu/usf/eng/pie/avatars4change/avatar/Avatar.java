package edu.usf.eng.pie.avatars4change.avatar;

import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

public class Avatar extends Entity {
	//values for choosing appropriate animations:
	private String activityLevel = "sleeping";
	private String activityName = "inBed";
	private int realismLevel;
	
	float scale;
	
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String spriteDir = baseFileDirectory + "/sprites";
	
	Location headL, bodyL;
	String headFile = spriteDir+"/face/default/.0.png";
	Sprite headSprite;
	String headName = "head";
	String bodyName = "body";
	String bodyDir  = spriteDir+"/body/default/.";
	Animation bodyAnim;
	
	//constructor
	public Avatar(Resources r, int realismL, String activityL) {
			activityLevel = activityL;
			realismLevel = realismL;
			super.addSprite( headSprite );
			super.addAnimation( bodyAnim );
			setupAvatar();
	}
	
	// === ACTIVITY LEVEL ===
	public String getActivityLevel(){
		return activityLevel;
	}
	public void setActivityLevel(String newLevel){
		activityLevel = newLevel;
		if(!this.isOkay()){		//if level does not match activity
			randomActivity(activityLevel);		//choose random activity in new level
		}//else don't worry about it
		setupAvatar();		//update bitmaps
	}
	// === REALISM LEVEL === 
	public int getRealismLevel(){
		return realismLevel;
	}
	public void setRealismLevel(int newLevel){
		realismLevel = newLevel;
		Log.d("MirrorMe Avatar", "Realism Level set to " + realismLevel);
		//update bitmaps
		setupAvatar();
	}
	
	//sets up the locations and sizes of the images for the avatar. Images are retrieved and drawn in the drawAvatar() method
	//  must be called whenever activity/realism levels change to update locations and scales of images!
	private void setupAvatar(){
		Log.d("MirrorMe Avatar","R:" + realismLevel + " A:" + activityLevel);
		
	}
	
	// === ACTIVITY NAME ===
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String newName) {
		//set new activity name
		activityName = newName;
		if(!this.isOkay()){	//change activity level if needed
			if(newName.equals("basketball") || newName.equals("running") || newName.equals("bicycling")){
				this.setActivityLevel("active");
			} else if(newName.equals("onComputer") || newName.equals("videoGames") || newName.equals("watchingTV")){
				this.setActivityLevel("passive");
			} else if (newName.equals("inBed")){
				this.setActivityLevel("sleeping");
			} else {
				Log.e("Avatars4Change Avatar", "activity name not recognized");
				return;
			}
		}
		setupAvatar();
	}
	
	//sets random activity name in the level passed
	public void randomActivity(String level){
		String activity = "this is not an activity";
    	//TODO: get these values from file directory on sdcard instead of having them as hardcoded strings
		//TODO: replace this with better random function
    	int newlevel = (int) (Math.floor((Math.random()*2.99999999999)));
    	if(level.equals("active")){
        	if(newlevel == 0){
        		activity = "basketball";
        	}else if(newlevel == 1){
        		activity = "running";
        	}else if(newlevel == 2){
        		activity = "bicycling";
        	}
        } else if(level.equals("passive")){
        	if(newlevel == 0){
        		activity = "watchingTV";
        	}else if(newlevel == 1){
        		activity = "videoGames";
        	}else if(newlevel == 2){
        		activity = "onComputer";
        	}
        } else if(level.equals("sleeping")){
        	activity = "inBed";
        } else {	//activity level is probably the default 'sleeping'
        	//TODO: something
        }
    	Log.v("MirrorMe Avatar", "new activity = " + activity);
    	setActivityName(activity);
	}
	
	public int maxH(){		//returns max height of avatar image
		return 200;
	}
	public int maxW(){		//returns max width of avatar image
		return 200;
	}
	
	//returns true if current animation name and activity are compatible
	public boolean isOkay(){
		if(activityLevel.equals("sleeping")){
			if(activityName.equals("inBed")){
				return true;
			}else return false;
		}else if(activityLevel.equals("active")){
			if (activityName.equals("basketball") || activityName.equals("running") || activityName.equals("bicycling")){
				return true;
			}else return false;
		}else if(activityLevel.equals("passive")){
			if(activityName.equals("onComputer") || activityName.equals("videoGames") || activityName.equals("watchingTV")){
				return true;
			}else return false;
		}else return false;	//activity level not recognized
	}	
	
	
	//display is 160 display-independent pixels, numerical (non-var) values below can be thought of as pixel values in the 160 pixel display
	public void loadPositions(String activName){
		
		headFile = ( baseFileDirectory + "/sprites/face/default/.0.png");		//create sprites
		bodyDir = ( baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/.");
		
		bodyL.set(0,0,160,0);	//body always in center
		// === ACTIVE ===
		if(activName.equals("running")){
			// === BODY ===
			// === HEAD ===
			headL.set(-13,-60,40,0);
		} else if(activName.equals("basketball")){
			// === BODY ===
			// === HEAD ===
			headL.set(8,16,15,0);
		} else if(activName.equals("bicycling")){
			// === BODY ===
			// === HEAD ===
			headL.set(-20,-60,40,0);
		// === ASLEEP ===
		} else if(activName.equals("inBed")){
			// === BODY ===
			// === HEAD ===
			headL.set(100,0,25,0);
		// === PASSIVE ===
		} else if(activName.equals("onComputer")){
			// === BODY ===
			// === HEAD ===
			headL.set(18,-32,33,0);
		} else if(activName.equals("videoGames")){
			// === BODY ===
			// === HEAD ===
			headL.set(57,-23,17,0);
		} else if(activName.equals("watchingTV")){
			// === BODY ===
			// === HEAD ===
			headL.set(10,-50,30,0);
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
			// === BODY ===
			// === HEAD ===
			headL.set(0,0,100,180);
		}
		headL.size = Math.round(headL.size*scale);
		bodyL.size = Math.round(bodyL.size*scale);
		headSprite.set(headName, headFile, headL);
		bodyAnim.set(bodyName, bodyDir, bodyL);
	}
	public void setScale( float newScale ){
		scale = newScale;
	}
}
