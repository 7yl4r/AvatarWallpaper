package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

public class avatarObject extends avatarWallpaper {	//TODO: this does NOT extend avatarWallpaper
	//--- fields ---------------------------
	//resource object for loading bitmaps from gen files
	//values for choosing appropriate animations:
	private String activityLevel = "sleeping";
	private String activityName = "inBed";
	private int realismLevel;
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String currentSpriteName = baseFileDirectory + "/sprites";
	
	//TODO: fix this here sprite stuff
	//bitmap arrays for sprites:
	sprite head = new sprite();		//create sprites
	sprite body = new sprite( baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/.");
/*	
	//SPRITE STUFF ---------------------------------------------
	private int currentFrame = 0;	//current frame of the animation 
	
	private float scaler = 1;
	private float faceScaleX = 1;
	private float faceScaleY = 1;
	

	
	//center locations of sprites:
	private int faceX = 0;
	private int faceY = 120;
	private int bodyX = 0;
	private int bodyY = 0;
	private Bitmap body = null;
	private Bitmap face = null;
	
	//END SPRITE STUFF ------------------------------------------
	 * */
	
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
			setupAvatar();		//update bitmaps
		}//else don't worry about it
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
	
	//sets up the locations and sizes of the images for the avatar. Images are retreieved and drawn in the drawAvatar() method
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
	
	//params: canvas upon which to draw, size of surface in X direction, size of surface in Y direction
	public void drawAvatar(Canvas c, float surfaceX, float surfaceY){
		/*
		//debug print output
		Log.d("MirrorMe Avatar","CURRENTFRAME:" + currentFrame);
		 */
		Bitmap sprite;
		if(backgroundOn){
			//draw background
		}
		if(bodyOn){
			//load in images from MirrorMe sdcard directory
			body.load();
			body.draw();
		}
		if(faceOn){
			//load face
			currentSpriteName = baseFileDirectory + "/sprites/face/default/.0.png";
			//load in images from MirrorMe sdcard directory
			face = BitmapFactory.decodeFile(currentSpriteName);
			if(face == null){
				currentSpriteName = baseFileDirectory + "/sprites/face/default/.0.png";				//load in images from MirrorMe sdcard directory
				face = BitmapFactory.decodeFile(currentSpriteName);
				if(face == null){	//if still null
					Log.e("MirrorMe Avatar", "face sprite at " + currentSpriteName +" not found!");	//something went wrong
				}
			}
			//draw face
			sprite = face;
			if(face == null){	//TODO: handle this error better!
				Log.v("MirrorMe Avatar Draw", "ERROR: Problem Loading face Sprites");
			} else{
				source = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
				dest = new Rect((int) (faceX-faceScaleX),
								(int) (faceY-faceScaleY),
								(int) (faceX+faceScaleX),
								(int) (faceY+faceScaleY));
				c.drawBitmap(sprite, source, dest, null);
				//c.drawBitmap(sprite,bodyX-sprite.getWidth()/2,bodyY-sprite.getHeight()/2,null);
			}
			
		}
		
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
			currentFrame++;
	}
	
	private void loadPositions(String activName){
		// === ACTIVE ===
		if(activName.equals("running")){
			faceX = -45;
			faceY = -220;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*25;
			faceScaleY = scaler*25;
		} else if(activName.equals("basketball")){
			faceX = 65;
			faceY = 60;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*8;
			faceScaleY = scaler*8;

		} else if(activName.equals("bicycling")){
			faceX = -65;
			faceY = -205;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*25;
			faceScaleY = scaler*20;
			// === ASLEEP ===
		} else if(activName.equals("inBed")){
			faceX = 300;	//OFF SCREEN
			faceY = 300;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*15;
			faceScaleY = scaler*20;
			// === PASSIVE ===
		} else if(activName.equals("onComputer")){
			faceX = 75;
			faceY = -120;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*10;
			faceScaleY = scaler*20;
		} else if(activName.equals("videoGames")){
			faceX = 185;
			faceY = -85;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*14;
			faceScaleY = scaler*20;
		} else if(activName.equals("watchingTV")){
			faceX = 105;
			faceY = -170;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*25;
			faceScaleY = scaler*20;
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe location setup","activity name not recognized");
			faceX = 0;
			faceY = 0;
			bodyX = 0;
			bodyY = 0;
			faceScaleX = scaler*5;
			faceScaleY = scaler*5;
		}
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
	
	public void setScaler(float newScale){
		scaler = newScale;
		//setupAvatar not needed, since scale is used in drawAvatar()
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
}
