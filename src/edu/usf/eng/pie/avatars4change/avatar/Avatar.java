package edu.usf.eng.pie.avatars4change.avatar;

import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

public class Avatar extends Entity {
	//avatar properties:
    public String    behaviorSelectorMethod = "constant";
    public long      lastActivityChange     = 0;	//last time activity level was changed [ms]
    public int       bedTime             = 23;
    public int       wakeTime            = 5;
	long lastFrameChange      = 0;		//last frame update [ms]
	long lastUserStatusUpdate = 0;
	long UPDATE_FREQUENCY     = 1000 * 10; 	//once per UPDATE_FREQUENCY; e.g. once/10s * 1s/1000ms
	
	//values for choosing appropriate animations:
	private String activityLevel = "sleeping";
	private String activityName  = "inBed";
	private int realismLevel = 111;
	
	// for finding the files:
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String spriteDir = baseFileDirectory + "/sprites";
	
	// object declarations for body parts:
	String headName = "head";
	Location headL = new Location();
	String headFile = spriteDir+"/face/default/0.png";

	String bodyTopName = "bodyTop";
	String bodyBottomName="bodyBottom";
	Location bodyL = new Location();
	String bodyDirBottom  = spriteDir+"/body/default/";
	String bodyDirTop  = bodyDirBottom;
	
	//constructor
	public Avatar(Location LOC, int realismL, String activityL) {
		super("AvatarObject",LOC);
		Log.v("Avatar","new avatar. R:" + realismLevel + " A:" + activityLevel);
		activityLevel = activityL;
		realismLevel = realismL;
		
		Log.v("Avatar","setting up "+name);
		// set up head
		headFile = ( baseFileDirectory + "/sprites/face/default/0.png");		//create sprites
		loadHeadLocation();
		//reloadHeadFiles()
		super.addSprite( headName, headFile, headL );
		
		reloadBodyFiles();
		
		// set up body
		bodyL = loadBodyLocation();
		bodyDirBottom = loadBodyDir("bottom");	//bottom layer
		bodyL.zorder = 0;
		super.addAnimation( bodyBottomName, bodyDirBottom, bodyL );

		//face layer is in middle

		bodyDirTop = loadBodyDir("top");
		bodyL.zorder = 2;
		super.addAnimation( bodyTopName, bodyDirTop, bodyL );		
	}
	
	private String loadBodyDir(String layerName){
		return ( baseFileDirectory + "/sprites/body/" + activityLevel + 
				"/" + activityName + "/"+layerName+"/");
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
	private void loadHeadLocation(){
		//this location should not show, and is a bit odd for easy debug
		Location LOC = new Location(0,0,1,30,180);	//center, layer 1, size 30, upside-down;
		int thisFrame = 0;
		if (super.getAnimation(bodyBottomName)!=null){
			thisFrame = super.getAnimation(bodyBottomName).currentFrame;
		}
		// === ACTIVE ===
		if(activityName.equals("running")){
			//LOC.set(-9,42,LOC.zorder,30,0);		//old used location
			LOC.set(-32, 106, LOC.zorder, 84, 0);
			//static location for all frames
		} else if(activityName.equals("basketball")){
			//load location based on current frame of body animation
			//find body animation
			thisFrame++; 	// frame number mysteriously lags by 1 for this animation
			switch (thisFrame){
				case 10:	//this frame is not real, but needed to account for lag fix
				case 0:
				case 1:
				case 2:
					LOC.set(20,-31,LOC.zorder,40,0);
					break;
				case 3:
					LOC.set(20,-21,LOC.zorder,40,0);
					break;
				case 4:
					LOC.set(21,-16,LOC.zorder,40,-10);
					break;
				case 5:
					LOC.set(20,-21,LOC.zorder,40,0);
					break;
				case 6:
					LOC.set(21,-29,LOC.zorder,40,0);
					break;
				case 7:
				case 8:
				case 9:
					LOC.set(21,-29,LOC.zorder,40,0);	
					break;
				default:
					Log.d("avatar head location", "bad frame number "+Integer.toString(thisFrame)+" for activity "+activityName);
					//LOC.set(6,-15,LOC.zorder,12,0);	//this is the old location	
					break;
			}
			
		} else if(activityName.equals("bicycling")){
			LOC.set(-33,90,LOC.zorder,90,-7);	//static
			//LOC.set(-13,42,LOC.zorder,30,-10);	//old
		// === ASLEEP ===
		} else if(activityName.equals("inBed")){
			LOC.set(0,0,LOC.zorder,0,0);	//size 0 so it won't show
			//LOC.set(100,0,LOC.zorder,25,0);	//old
		// === PASSIVE ===
		} else if(activityName.equals("onComputer")){
			LOC.set(37, 62, LOC.zorder, 79, 0);	//static
			//LOC.set(13,26,LOC.zorder,33,-5);	//old
		} else if(activityName.equals("videoGames")){
			LOC.set(108,50,LOC.zorder,50,0);	//static
			//LOC.set(37,17,LOC.zorder,17,0);	//old
		} else if(activityName.equals("watchingTV")){
			LOC.set(26,107,LOC.zorder,84,0);	//static
			//LOC.set(9,39,LOC.zorder,30,0);//old
			
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
			LOC.set(0,0,LOC.zorder,100,180);
		}
		headL = LOC;
		setSpriteLocation(headName,headL);
	}
	
	private Location loadBodyLocation(){
		//TODO: this is a hack-y fix. percent of total entity size should be 100% instead of 50%
		return new Location(0,0,0,300,0);//body always in center, full size of entity
	}
	
	//sets up new activity 
	private void reloadBodyFiles(){
		// === ACTIVE ===
		if(activityName.equals("running")){

			bodyDirTop = null;
			bodyDirBottom = spriteDir+"/body/active/running/";

		} else if(activityName.equals("basketball")){
			bodyDirTop = spriteDir+"/body/active/basketball/top/";
			bodyDirBottom = spriteDir+"/body/active/basketball/bottom/";
		} else if(activityName.equals("bicycling")){
			bodyDirTop = spriteDir+"/body/active/bicycling/top/";
			bodyDirBottom = spriteDir+"/body/active/bicycling/bottom/";
		// === ASLEEP ===
		} else if(activityName.equals("inBed")){

			bodyDirBottom = spriteDir+"/body/sleeping/inBed/";
			bodyDirTop    = null;
		// === PASSIVE ===
		} else if(activityName.equals("onComputer")){
			bodyDirTop = null;
			bodyDirBottom = spriteDir+"/body/passive/onComputer/";
			
		} else if(activityName.equals("videoGames")){
			bodyDirTop = null;
			bodyDirBottom = spriteDir+"/body/passive/videoGames/";
		} else if(activityName.equals("watchingTV")){
			bodyDirTop = null;
			bodyDirBottom = spriteDir+"/body/passive/watchingTV/";
			// === DEFAULT === 
		} else {
			Log.e("MirrorMe sprite","activity name not recognized");
	
			bodyDirTop = null;
			bodyDirBottom = spriteDir+"/body/default/";
		}
		super.setAnimationDir( bodyTopName, bodyDirTop);
		super.setAnimationDir( bodyBottomName, bodyDirBottom);
		super.resetFrameCount();
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
		loadHeadLocation();
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
	public void nextFrame(){
		loadHeadLocation();
		super.nextFrame();
	}
	
	@Override
	public void draw(Canvas c){
		reloadHeadFiles();	//need to reload head sprite in case it has changed
		
		//TODO: set the location of the head based on the frame number and the animation type
		
		super.draw(c);
	}
}
