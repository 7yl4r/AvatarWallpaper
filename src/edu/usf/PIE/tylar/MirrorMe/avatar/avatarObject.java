package edu.usf.PIE.tylar.MirrorMe.avatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import edu.usf.PIE.tylar.MirrorMe.R;

public class avatarObject extends avatarWallpaper {
	//--- fields ---------------------------
	//center locations of sprites:
	private int headX = 0;
	private int headY = 120;
	private int bodyX = 0;
	private int bodyY = 0;
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String currentSprite = baseFileDirectory + "/sprites";
	//resource object for loading bitmaps from gen files
	Resources res = null;	//TODO: remove this
	//values for choosing appropriate animations:
	private String activityLevel;
	private String activityName = "inBed";
	private int realismLevel;
	private int currentFrame = 0;	//current frame of the animation 
	//bitmap arrays for sprites:
	private Bitmap body = null;
	private Bitmap[] head = new Bitmap[10];
	//booleans determine if bitmaps are drawn:
	private boolean bodyOn = false;
	private boolean faceOn = false;
	private boolean backgroundOn = false;

	//constructor
	public avatarObject(Resources r, int realismL, String activityL) {
			activityLevel = activityL;
			realismLevel = realismL;
			res = r;
			loadBitmaps();
	}
	public String getActivityLevel(){
		return activityLevel;
	}
	public void setActivityLevel(String newLevel){
		activityLevel = newLevel;
		//update bitmaps
		loadBitmaps();
	}
	public int getRealismLevel(){
		return realismLevel;
	}
	public void setRealismLevel(int newLevel){
		realismLevel = newLevel;
		Log.d("MirrorMe Avatar", "Realism Level set to " + realismLevel);
		//update bitmaps
		loadBitmaps();
	}
	
	public void setResources(Resources r){
		res = r;
	}
	
	//loads bitmap files assuming all are saved in the ./assets folder with the following naming convention:
	//	r<realism_level>_a<activity_level>_<body part>_f<frame number>.png
	//  for example: realism=1, activity=3, left arm, frame = 11 would be:
	//	R1_A3_leftArm_F11.png
	//each animation is assumed to have 10 frames labeled F0->F9
	private void loadBitmaps(){
		//Log.d("MirrorMe Avatar", "RESOURCES PASSED TO avatarObject: " + res);
		Log.d("MirrorMe Avatar","R:" + realismLevel + " A:" + activityLevel + " F:" + currentFrame);
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
			faceOn = false;		//TODO: this should be true
			//body bitmap loading done every frame in drawAvatar() !!!
			
			loadPositions(activityName);	//set postions of sprites
			//load face (same for all cartoons)
			loadFace();
			
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
		Rect source, dest;
		Bitmap sprite;
		float scaler;
		if(backgroundOn){
			//draw background
		}
		if(bodyOn){
			//load body
			currentSprite = baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/" + String.valueOf(currentFrame) + ".png";
			//load in images from MirrorMe sdcard directory
			body = BitmapFactory.decodeFile(currentSprite);
			if(body == null){
				currentFrame = 0;	//animation loops back
				currentSprite = baseFileDirectory + "/sprites/body/" + activityLevel + "/" + activityName + "/" + String.valueOf(currentFrame) + ".png";
				//load in images from MirrorMe sdcard directory
				body = BitmapFactory.decodeFile(currentSprite);
				if(body == null){	//if still null
					Log.e("MirrorMe Avatar", "body sprite at " + currentSprite +" not found!");	//something went wrong
				}
			}
			//draw body
			sprite = body;
			scaler = 2;
			if(body == null){	//TODO: handle this error better!
				Log.v("MirrorMe Avatar Draw", "ERROR: Problem Loading Sprites");
			} else{
				source = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
				dest = new Rect((int) (bodyX-sprite.getWidth()/2*scaler),
								(int) (bodyY-sprite.getHeight()/2*scaler),
								(int) (bodyX+sprite.getWidth()/2*scaler),
								(int) (bodyY+sprite.getHeight()/2*scaler));
				c.drawBitmap(sprite, source, dest, null);
				//c.drawBitmap(sprite,bodyX-sprite.getWidth()/2,bodyY-sprite.getHeight()/2,null);
			}
		}
		if(faceOn){
			//draw head
			sprite = head[currentFrame];
			c.drawBitmap(sprite,-headX-sprite.getWidth()/2,-headY-sprite.getHeight()/2,null);
		}
		
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
		if(currentFrame >= 9){
			currentFrame = 0;
		} else{
			currentFrame++;
		}
	}
	
	/*
	private void loadRunningStickman(){
		body[0] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f0);
		body[1] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f1);
		body[2] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f2);
		body[3] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f3);
		body[4] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f4);
		body[5] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f5);
		body[6] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f6);
		body[7] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f7);
		body[8] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f8);
		body[9] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_body_f9);
	}
	
	private void loadCircle(){
		head[0] = BitmapFactory.decodeResource(res,R.drawable.r0_a3_head_f0);
		//set all to same bitmap
		for(int i = 1; i < 10; i++){
			head[i] = head[i-1];
		}
	}
	*/
	
	private void loadFace(){
		head[0] = BitmapFactory.decodeResource(res,R.drawable.r1_a3_head_f0);
		//set all to same bitmap
		for(int i = 1; i < 10; i++){
			head[i] = head[i-1];	
		}
	}
	
/*	TEMPORARILY DISABLED
	private void stickman(){
		bodyOn = true;
		faceOn = false;
		switch(activityLevel){	//select for activity level of stickman
			case 0:	//--- sleep ------------------------------------------------------------------
				Log.e("MirrorMe Avatar", "sleeping stickman not yet implemented");
				break;
			case 3:	// --- running ----------------------------------------------------------
				//body:
				loadRunningStickman();
				//face
				loadCircle();
				break;
			default:
				Log.d("MirrorMe Avtar", "This Stickman Activity case not yet implemented");
				break;
		}
	}
	
	private void stickmanNface(){
		bodyOn = true;
		faceOn = true;
		switch(activityLevel){
			case 0:	//--- sleep ------------------------------------------------------------------

			case 3: // --- basketball -------------------------------------------------------
				headY = 100;
				headX = 0;
				bodyY = 0;
				bodyX = 0;
				Log.e("MirrorMe Avatar", "stick basketball not yet implemented");
				break;
			case 4:	// --- running ----------------------------------------------------------
				headY = 120;
				headX = 0;
				bodyY = 0;
				bodyY = 0;
				//head bitmaps:
				loadFace();
				//body:
				loadRunningStickman();
				break;
			default:
				Log.e("MirrorMe Avatar", "this case not yet implemented");
				break;
		}
	}
	
	private void cartoon(){
		bodyOn = true;
		faceOn = false;
		switch(activityLevel){
			case 0:	// --- sleeping --------------------------------
				loadSleepingCartoon();
				break;
			case 3: // --- basketball ------------------------------
				loadBasketballCartoon();
				break;
			case 4: // --- running ----------------------------------
				loadRunningCartoon();
				break;
			default:
				Log.e("MirrorMe Avtar", "this cartoon case not yet implemented");
				break;
		}	
	}
	*/
	private void loadPositions(String activName){
		if(activName.equals("running")){
			headX = 20;
			headY = 110;
		}
	}
}
