package edu.usf.PIE.tylar.MirrorMe.avatar;

import java.util.Arrays;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

//import edu.usf.PIE.avatars4change.entity;

public class avatarObject extends avatarWallpaper {	//TODO: this does NOT extend avatarWallpaper... wait... yes it does? idk. clearly I am not an experienced Java programmer.
	//--- fields ---------------------------
	//resource object for loading bitmaps from gen files
	//values for choosing appropriate animations:
	private String activityLevel = "sleeping";
	private String activityName = "inBed";
	private int realismLevel;
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String spriteDir = baseFileDirectory + "/sprites";
	float scaler = 1;	//set to actualsize/defaultsize
	
	sprite head = new sprite( baseFileDirectory + "/sprites/face/default/.");		//create sprites
	sprite body = new sprite( baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/.");
	
	//booleans determine if bitmaps are drawn:
	private boolean bodyOn = false;
	private boolean faceOn = false;
	private boolean backgroundOn = false;

	//constructor
	public avatarObject(Resources r, int realismL, String activityL) {
			activityLevel = activityL;
			realismLevel = realismL;
			setupAvatar();
	}
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
		//Log.d("MirrorMe Avatar", "RESOURCES PASSED TO avatarObject: " + res);
		Log.d("MirrorMe Avatar","R:" + realismLevel + " A:" + activityLevel);
		switch(realismLevel){	//select for level of realism
		case 0:	// === stickman ====================================================================
			/*	TEMPORARILY DISABLED
			stickman();
			break;
			*/
		case 1: // === stickman with user face ========================================================
			/*
			stickmanNface();
			break;
		*/
		case 2: // === realistic cartoon avatar ============================================================
			/* TEMPORARILY DISABLED
			cartoon();
			break;
			*/
		case 3: // === realistic cartoon avatar with face =======================================================
			bodyOn = true;
			faceOn = true;
			//body bitmap loading done every frame in drawAvatar()
			//face bitmap loaded in drawAvatar
			loadPositions(getActivityName());	//set positions of sprites
			
			break;
			/* TEMPORARILY DISABLED
		case 4: // === actual recording of subject=====================================================
			bodyOn = false;
			faceOn = false;
			Log.e("MirrorMe Avatar", "Realism Level 4 not yet implemented");
			break;
			*/
		default:
			Log.e("MirrorMe Avatar", "This Realism level not yet implemented");
			break;
		}
	}
	
	//params: canvas upon which to draw set to origin in center, size of surface in X direction, size of surface in Y direction
	public void drawAvatar(Canvas c, float surfaceX, float surfaceY){
		//Log.d("MirrorMe Avatar","CURRENTFRAME:" + currentFrame);	//log for debugging
		if(backgroundOn){
			//draw background
		}
		if(bodyOn){
			body.draw(c,surfaceX,surfaceY);
		}
		if(faceOn){
			head.draw(c,surfaceX,surfaceY);
		}
		
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
			body.nextFrame();
			head.nextFrame();
	}
	

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
	
	public void setScaler(float newScale){
		scaler = newScale;
	}
	
	//display is 160 display-independent pixels, numerical (non-var) values below can be thought of as pixel values in the 160 pixel display
	public void loadPositions(String activName){
		head.load( baseFileDirectory + "/sprites/face/default/.");		//create sprites
		body.load( baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/.");
		head.nFrames = 1;
		Arrays.fill(body.x, 0);	//center?
		Arrays.fill(body.y, 0);
		Arrays.fill(body.sx,(int)Math.round(scaler*160));
		Arrays.fill(body.sy,(int)Math.round(scaler*160));
		// === ACTIVE ===
		if(activName.equals("running")){
			// === BODY ===
			body.nFrames = 12;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*-13));
			Arrays.fill(head.y, (int)Math.round(scaler*-60));
			Arrays.fill(head.sx, (int)Math.round(scaler*40));
			Arrays.fill(head.sy, (int)Math.round(scaler*40));
		} else if(activName.equals("basketball")){
			// === BODY ===
			body.nFrames = 10;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*8));
			Arrays.fill(head.y, (int)Math.round(scaler*16));
			Arrays.fill(head.sx, (int)Math.round(scaler*15));
			Arrays.fill(head.sy, (int)Math.round(scaler*15));
		} else if(activName.equals("bicycling")){
			// === BODY ===
			body.nFrames = 9;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*-20));
			Arrays.fill(head.y, (int)Math.round(scaler*-60));
			Arrays.fill(head.sx, (int)Math.round(scaler*40));
			Arrays.fill(head.sy, (int)Math.round(scaler*40));
		// === ASLEEP ===
		} else if(activName.equals("inBed")){
			// === BODY ===
			body.nFrames = 10;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*100));
			Arrays.fill(head.y, (int)Math.round(scaler*0));
			Arrays.fill(head.sx, (int)Math.round(scaler*25));
			Arrays.fill(head.sy, (int)Math.round(scaler*25));
		// === PASSIVE ===
		} else if(activName.equals("onComputer")){
			// === BODY ===
			body.nFrames = 6;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*18));
			Arrays.fill(head.y, (int)Math.round(scaler*-32));
			Arrays.fill(head.sx, (int)Math.round(scaler*33));
			Arrays.fill(head.sy, (int)Math.round(scaler*33));
		} else if(activName.equals("videoGames")){
			// === BODY ===
			body.nFrames = 4;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*57));
			Arrays.fill(head.y, (int)Math.round(scaler*-23));
			Arrays.fill(head.sx, (int)Math.round(scaler*17));
			Arrays.fill(head.sy, (int)Math.round(scaler*17));
		} else if(activName.equals("watchingTV")){
			// === BODY ===
			body.nFrames = 10;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*10));
			Arrays.fill(head.y, (int)Math.round(scaler*-50));
			Arrays.fill(head.sx, (int)Math.round(scaler*30));
			Arrays.fill(head.sy, (int)Math.round(scaler*30));
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
			// === BODY ===
			body.nFrames = 0;
			//body loc const
			// === HEAD ===
			//head frames const
			Arrays.fill(head.x, (int)Math.round(scaler*0));
			Arrays.fill(head.y, (int)Math.round(scaler*10));
			Arrays.fill(head.sx, (int)Math.round(scaler*15));
			Arrays.fill(head.sy, (int)Math.round(scaler*20));
		}
	}
}
