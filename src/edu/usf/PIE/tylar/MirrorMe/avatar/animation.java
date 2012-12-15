package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class animation {
	int MAXFRAMES = 15;		//maximum number of frames in the sprite
	
	//animation frames
	int currentFrame = 0;
	public int nFrames = 0;	//number of frames
	
	String fileDir = "notAFileName";	//init string (overwritten in constructor
	String name = "UNNAMED";
		
	//sprite frame[];	//frames in the animation are sprites
	Bitmap currentImage; 
	
	location L;
	
	//constructor
	public animation(String newName, String fDir, location newLocation) {
		name = newName;
		fileDir = fDir;	//set file directory
		L = newLocation;
		this.load();
		//nFrames = newFrameCount;	//TODO set number of frames
		//allocate space for arrays???
		//set up spriteSheet???
	}
	
	//loads current animation image
	public void load(){
		//for each frame 
		
		//set up the frame
		
		
		currentImage = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
		if(currentImage == null){
			currentFrame = 0;	//animation loops back
			//load in images from MirrorMe sdcard directory
			currentImage = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
			if(currentImage == null){	//if still null
				Log.e("MirrorMe Avatar Sprite", "body sprite at " + fileDir+currentFrame+".png" +" not found!");	//something went wrong
			}
		}
	}
	
	public void addFrame(){
		
	}
	
	
	public void draw(Canvas c){
		if(currentImage == null){
			Log.e("sprite","cannot draw sprite, no image!");
			return;	//don't draw if no image
		}
		Rect source, dest;
		source = new Rect(0, 0, currentImage.getWidth(), currentImage.getHeight());
		
		int w = 0,h = 0;	//image width & height (actually radius of image)
		if(currentImage.getWidth()>currentImage.getHeight()){ 
			w = L.size;
			h = Math.round( (float)L.size * ((float)currentImage.getHeight()/(float)currentImage.getWidth()) );
		} else {
			h = L.size;
			w = Math.round( (float)L.size * ((float)currentImage.getWidth()/(float)currentImage.getHeight()) );
		}
		dest = new Rect(L.x-w/2, L.y-h/2, L.x+w/2, L.y+h/2);
		Log.d("sprite","w=" + Integer.toString(w) + " h=" + Integer.toString(h));
		c.rotate(L.rotation);
		c.drawBitmap(currentImage, source, dest, null);
		c.rotate(-L.rotation);
	}
	
	public void nextFrame(){
		currentFrame++;
		this.load();
	}
}
