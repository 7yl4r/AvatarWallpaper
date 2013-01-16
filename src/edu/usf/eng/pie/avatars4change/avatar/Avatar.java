package edu.usf.eng.pie.avatars4change.avatar;

import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

public class Avatar extends Entity {
	//values for choosing appropriate animations:
	private String activityLevel = "sleeping";
	private String activityName  = "inBed";
	private int realismLevel = 111;
	
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String spriteDir = baseFileDirectory + "/sprites";
	
	String headName = "head";
	Location headL = new Location();
	String headFile = spriteDir+"/face/default/.0.png";
//	Sprite headSprite = new Sprite(headName,headFile,headL);

	String bodyName = "body";
	Location bodyL = new Location();
	String bodyDir  = spriteDir+"/body/default/.";
//	Animation bodyAnim = new Animation(bodyName,bodyDir,bodyL);
	
	//constructor
	public Avatar(Location LOC, int realismL, String activityL) {
		super("AvatarObject",LOC);
		Log.v("Avatar","new avatar. R:" + realismLevel + " A:" + activityLevel);
		activityLevel = activityL;
		realismLevel = realismL;
		
		Log.v("Avatar","setting up "+name);
		// set up head
		headFile = ( baseFileDirectory + "/sprites/face/default/.0.png");		//create sprites
		headL = loadHeadLocation();
		//reloadHeadFiles()
		super.addSprite( headName, headFile, headL );
		
		// set up body
		bodyDir = ( baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/.");
		bodyL = loadBodyLocation();
		reloadBodyFiles();
		super.addAnimation( bodyName, bodyDir, bodyL );
	}
	
	// === ACTIVITY LEVEL ===
	public String getActivityLevel(){
		return activityLevel;
	}
	public void setActivityLevel(String newLevel){
		activityLevel = newLevel;
		Log.v("Avatar",name+" activity level set to "+activityLevel);
		if(!this.isOkay()){		//if level does not match activity
			randomActivity(activityLevel);		//choose random activity in new level
		}//else don't worry about it
	}
	// === REALISM LEVEL === 
	public int getRealismLevel(){
		return realismLevel;
	}
	public void setRealismLevel(int newLevel){
		realismLevel = newLevel;
		Log.v("Avatar",name+" realism level set to "+realismLevel);
		//TODO reload avatar if needed
	}
	
	//sets up the locations and sizes of the images for the avatar. Images are retrieved and drawn in the drawAvatar() method
	//  must be called whenever activity/realism levels change to update locations and scales of images!
	private Location loadHeadLocation(){
		Location LOC = new Location();
		// === ACTIVE ===
		if(activityName.equals("running")){
			// === BODY ===
			// === HEAD ===
			LOC.set(-9,42,LOC.zorder,30,0);
		} else if(activityName.equals("basketball")){
			// === BODY ===
			// === HEAD ===
			LOC.set(6,-15,LOC.zorder,12,0);
		} else if(activityName.equals("bicycling")){
			// === BODY ===
			// === HEAD ===
			LOC.set(-13,42,LOC.zorder,30,-10);
		// === ASLEEP ===
		} else if(activityName.equals("inBed")){
			// === BODY ===
			// === HEAD ===
			LOC.set(100,0,LOC.zorder,25,0);
		// === PASSIVE ===
		} else if(activityName.equals("onComputer")){
			// === BODY ===
			// === HEAD ===
			LOC.set(13,26,LOC.zorder,33,-5);
		} else if(activityName.equals("videoGames")){
			// === BODY ===
			// === HEAD ===
			LOC.set(37,17,LOC.zorder,17,0);
		} else if(activityName.equals("watchingTV")){
			// === BODY ===
			// === HEAD ===
			LOC.set(9,39,LOC.zorder,30,0);
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
			// === BODY ===
			// === HEAD ===
			LOC.set(0,0,LOC.zorder,100,180);
		}
		return new Location(scaleValueFromPercent(LOC.x),scaleValueFromPercent(LOC.y),LOC.zorder,scaleValueFromPercent(LOC.size),LOC.rotation);
	}
	
	private Location loadBodyLocation(){
		//TODO: this is a hack-y fix. percent of total entity size should be 100% instead of 50%
		return new Location(0,0,0,scaleValueFromPercent(50),0);//body always in center, full size of entity
	}
	
	private void reloadBodyFiles(){
		// === ACTIVE ===
		if(activityName.equals("running")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/active/running/.";
		} else if(activityName.equals("basketball")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/active/basketball/.";
		} else if(activityName.equals("bicycling")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/active/bicycling/.";
		// === ASLEEP ===
		} else if(activityName.equals("inBed")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/sleeping/inBed/.";
		// === PASSIVE ===
		} else if(activityName.equals("onComputer")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/passive/onComputer/.";
		} else if(activityName.equals("videoGames")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/passive/videoGames/.";
		} else if(activityName.equals("watchingTV")){
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/passive/watchingTV/.";
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
			// === BODY ===
			// === HEAD ===
			bodyDir = spriteDir+"/body/default/.";
		}
		super.setAnimationDir( bodyName, bodyDir);
	}
	
	private void reloadHeadFiles(){
		super.setSpriteFile(headName, headFile);
	}
	
	// === ACTIVITY NAME ===
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String newName) {
		activityName = newName;		//set new activity name
		Log.v("Avatar",name+" activity set to "+activityName);
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
		reloadBodyFiles();
		headL = loadHeadLocation();
		setSpriteLocation(headName,headL);
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
        } else {
        	Log.e("avatar","activity level "+activity+" not recognized");
        }
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
	
	@Override
	public void draw(Canvas c){
		reloadHeadFiles();	//need to reload head sprite in case it has changed
		super.draw(c);
	}
}
