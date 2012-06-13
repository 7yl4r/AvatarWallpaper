package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import edu.usf.PIE.tylar.MirrorMe.R;


public class avatarObject extends avatarWallpaper {
	//fields
	private int currentFrame = 0;
	private int headX = 0;
	private int headY = 120;
	private int bodyX = 0;
	private int bodyY = 0;
	Resources res = null;
	private int activityLevel;
	private int realismLevel;
	private Bitmap[] body = new Bitmap[10];
	public Bitmap[] head = new Bitmap[10];

	
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
		Log.d("MirrorMe Avatar", "RESOURCES PASSED TO avatarObject: " + res);
		Log.d("MirrorMe Avatar","R:" + realismLevel + " A:" + activityLevel + " F:" + currentFrame);
		switch(realismLevel){
		case 0:	// === stickman ====================================================================
			switch(activityLevel){
			/*
			case 0:	//--- sleep ------------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f9);
				break;
			case 1://--- uhmmm ------------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f9);
				break;
			case 2: //--- somethin --------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f9);
				break;
			*/
			case 3:	// --- running ----------------------------------------------------------
				/*
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a3_head_f0);
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
				//body:
				loadRunningStickman();
				//face
				loadCircle();
				break;
			//TODO: default case should show error
			}
		case 1: // === stickman with user face ========================================================
			switch(activityLevel){
			/*
			case 0:	//--- sleep ------------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a0_body_f9);
				break;
			case 1://--- uhmmm ------------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a1_body_f9);
				break;
			case 2: //--- somethin --------------------------------------------------------------
				//head bitmaps:
				head[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f0);
				head[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f1);
				head[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f2);
				head[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f3);
				head[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f4);
				head[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f5);
				head[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f6);
				head[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f7);
				head[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f8);
				head[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_head_f9);
				//body bitmaps:
				body[0] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f0);
				body[1] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f1);
				body[2] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f2);
				body[3] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f3);
				body[4] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f4);
				body[5] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f5);
				body[6] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f6);
				body[7] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f7);
				body[8] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f8);
				body[9] = BitmapFactory.decodeResource(getResources(),R.drawable.r0_a2_body_f9);
				break;
			*/
			case 3:	// --- running ----------------------------------------------------------
				headY = 120;
				headX = 0;
				bodyY = 0;
				bodyY = 0;
				//head bitmaps:
				loadFace();
				//body:
				loadRunningStickman();
				break;
			//TODO: default case should show error
			}
		case 2: // === ?something?more?realistic? ============================================================
			 
		case 3: // === realistic cartoon avatar =======================================================
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
			
			head[0] = BitmapFactory.decodeResource(res,R.drawable.r3_a3_head_f0);
			//set all to same bitmap
			for(int i = 1; i < 10; i++){
				head[i] = head[i-1];
			}
			headX = 20;
			headY = 110;
			break;
		case 4: // === actual recording of subject=====================================================
			break;
		//TODO: default case should show error
		}
	}

	
	//params: canvas upon which to draw, size of surface in X direction, size of surface in Y direction
	public void drawAvatar(Canvas c, float surfaceX, float surfaceY){
		/*OLD CODE:
		            //get avatar bitmap from bitmap arrays generated previously from resources
            Bitmap avatar = null;
            switch (level_of_activity) {
            	case 0:
            		if(currentFrame >= sleeping.length){
            			currentFrame = 0;
            		}
            		avatar = sleeping[currentFrame];;
            		break;
            	case 1:
            		avatar = BitmapFactory.decodeResource(getResources(),R.drawable.one_0);
            		break;
            	case 2:
            		avatar = BitmapFactory.decodeResource(getResources(),R.drawable.two_0);
            		break;
            	case 3:
            		//to animate the avatar:
            		switch(currentFrame){
            			case 12: currentFrame = 0;	//reset frame counter
            			case 0:
            				avatar = running[0];
                    		break;
            			case 1: 
            			case 11:
            				avatar = running[1];
                    		break;
            			case 2:
            			case 10:
            				avatar = running[2];
                    		break;
            			case 3:
            			case 9:
            				avatar = running[3];
                    		break;
            			case 4:
            			case 8:
            				avatar = running[4];
                    		break;
            			case 5:
            			case 7:
            				avatar = running[5];
                    		break;
            			case 6:
            				avatar = running[6];
                    		break;
            		}
            	//etc...
        		//TODO: default should show error
            }
           
            if(avatar != null){
            	//draw the bitmap in the middle of the screen, assuming canvas origin set to center
            	c.drawBitmap(avatar,-avatar.getWidth()/2,-avatar.getHeight()/2,null);
            }
		 */
		
		/*
		//debug print output
		Log.d("MirrorMe Avatar","CURRENTFRAME:" + currentFrame);
		 */
		//draw background
		
		//draw body
		Bitmap sprite = body[currentFrame];
		c.drawBitmap(sprite,bodyX-sprite.getWidth()/2,bodyY-sprite.getHeight()/2,null);
		
		//draw head
		sprite = head[currentFrame];
		c.drawBitmap(sprite,-headX-sprite.getWidth()/2,-headY-sprite.getHeight()/2,null);
		
	}
	
	public void nextFrame(){
		if(currentFrame >= 9){
			currentFrame = 0;
		} else{
			currentFrame++;
		}
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
}
