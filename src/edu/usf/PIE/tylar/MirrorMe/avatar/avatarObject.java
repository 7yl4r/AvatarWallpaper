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
	String fileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	//resource object for loading bitmaps from gen files
	Resources res = null;	//TODO: remove this
	//values for choosing appropriate animations:
	private int activityLevel;
	private int realismLevel;
	private int currentFrame = 0;	//current frame of the animation 
	//bitmap arrays for sprites:
	private Bitmap[] body = new Bitmap[10];
	private Bitmap[] head = new Bitmap[10];
	//booleans determine if bitmaps are drawn:
	private boolean bodyOn = false;
	private boolean faceOn = false;
	private boolean backgroundOn = false;

	//constructor
	public avatarObject(Resources r, int realismL, int activityL) {
			activityLevel = activityL;
			realismLevel = realismL;
			res = r;
			loadBitmaps();
	}
	public int getActivityLevel(){
		return activityLevel;
	}
	public void setActivityLevel(int newLevel){
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
		currentFrame = 0;
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
			faceOn = false;
			//load face (same for all cartoons)
			loadFace();
			switch(activityLevel){
				case 0: // --- sleeping ------------------------------------------
					//TODO: set position & scale
					loadSleepingCartoon();
					break;
				case 1: // --- passive activity ----------------------------------
					
				case 2: // --- active activity -----------------------------------
				case 3: // --- Basketball ----------------------------------------
					//TODO: set position & scale
					loadBasketballCartoon();
					break;
				case 4: // --- running -------------------------------------------
					//TODO: set position & scale
					//load body
					loadRunningCartoon();
					headX = 20;
					headY = 110;
					break;
				default:
					Log.e("MirrorMe Avatar", "this cartoon with face case not yet implemented");
					break;
			}
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
			//draw body
			sprite = body[currentFrame];
			scaler = 2;
			source = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
			dest = new Rect((int) (bodyX-sprite.getWidth()/2*scaler),
							(int) (bodyY-sprite.getHeight()/2*scaler),
							(int) (bodyX+sprite.getWidth()/2*scaler),
							(int) (bodyY+sprite.getHeight()/2*scaler));
			c.drawBitmap(sprite, source, dest, null);
			//c.drawBitmap(sprite,bodyX-sprite.getWidth()/2,bodyY-sprite.getHeight()/2,null);
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
	
	private void loadSleepingCartoon(){
		//load in image from MirrorMe sdcard directory
		
		/*String[] imageInSD = {fileDirectory + "/sprites/sleep/f0.PNG",
								fileDirectory + "/sprites/sleep/f1.PNG"};
		body[0] = BitmapFactory.decodeFile(imageInSD[0]);
        //TODO: this this this
		*/
		//old way of loading from resources
		body[0] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f0);
		body[1] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f1);
		body[2] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f2);
		body[3] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f3);
		body[4] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f4);
		body[5] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f5);
		body[6] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f6);
		body[7] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f7);
		body[8] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f8);
		body[9] = BitmapFactory.decodeResource(res,R.drawable.sleeping_body_f9);
	}
	
	private void loadRunningCartoon(){
		body[0] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f0);
		body[1] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f1);
		body[2] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f2);
		body[3] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f3);
		body[4] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f4);
		body[5] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f5);
		body[6] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f6);
		body[7] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f7);
		body[8] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f8);
		body[9] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_body_f9);
	}
	
	private void loadBasketballCartoon(){
		body[0] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f0);
		body[1] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f1);
		body[2] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f2);
		body[3] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f3);
		body[4] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f4);
		body[5] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f5);
		body[6] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f6);
		body[7] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f7);
		body[8] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f8);
		body[9] = BitmapFactory.decodeResource(res,R.drawable.bball_body_f9);
	}
	
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
	
	private void loadFace(){
		head[0] = BitmapFactory.decodeResource(res,R.drawable.r1_a3_head_f0);
		//set all to same bitmap
		for(int i = 1; i < 10; i++){
			head[i] = head[i-1];	
		}
		/*
		head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f1);
		head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f2);
		head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f3);
		head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f4);
		head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f5);
		head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f6);
		head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f7);
		head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f8);
		head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f9);
		*/
	}
	
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
}
