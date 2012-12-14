package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class sprite {
	int MAXFRAMES = 15;		//maximum number of frames in the sprite
	
	String name = "UNNAMED";
	String fileDir = "notAFileName";	//init string (overwritten in constructor
	location L = new location();
	
	Bitmap image;
	
	//animation frames
	int currentFrame = 0;
	public int nFrames = 0;	//number of frames
		
	//center locations
	int x[] = new int[MAXFRAMES];
	int y[] = new int[MAXFRAMES];
	
	//sizes
	int sx[] = new int[MAXFRAMES];
	int sy[] = new int[MAXFRAMES];
	
	//rotation
	int theta = 0;

	public sprite(String nam, String fDir, location L) {
			fileDir = fDir;	//set file directory
			name = nam;
			//TODO set location
			//TODO set number of frames
			//allocate space for arrays
			this.load();
	}
	
	//loads current animation image
	public void load(){	
		image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
		if(image == null){
			currentFrame = 0;	//animation loops back
			//load in images from MirrorMe sdcard directory
			image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
			if(image == null){	//if still null
				Log.e("MirrorMe Avatar Sprite", "body sprite at " + fileDir+currentFrame+".png" +" not found!");	//something went wrong
			}
		}
	}

	public void load(String fDir){
		fileDir = fDir;
		image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
		if(image == null){
			currentFrame = 0;	//animation loops back
			//load in images from MirrorMe sdcard directory
			image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
			if(image == null){	//if still null
				Log.e("MirrorMe Avatar Sprite", "body sprite at " + fileDir+currentFrame+".png" +" not found!");	//something went wrong
			}
		}		
	}
	
	//TODO this function is outdated, needs to be removed???
	public void draw(Canvas c, float surfaceX, float surfaceY){
		if(image == null)	return;	//don't draw if no image
		Rect source, dest;
		source = new Rect(0, 0, image.getWidth(), image.getHeight());
		dest = new Rect((int)(x[currentFrame]-sx[currentFrame]/2),
						(int)(y[currentFrame]-sy[currentFrame]/2),
						(int)(x[currentFrame]+sx[currentFrame]/2),
						(int)(y[currentFrame]+sy[currentFrame]/2));
		c.drawBitmap(image, source, dest, null);	
	}
	
	//draws the sprite on given canvas c at object location relative to given location L
	public void draw(Canvas c){
		if(image == null)	return;	//don't draw if no image
		Rect source, dest;
		source = new Rect(0, 0, image.getWidth(), image.getHeight());//TODO to use sprite sheet, adjust this to select part of image
		int w = 0,h = 0;	//image width & height (actually radius of image)
		if(image.getWidth()>image.getHeight()){ 
			w = L.scale;
			h = L.scale * (image.getHeight()/image.getWidth());
		} else {
			h = L.scale;
			w = L.scale * (image.getWidth()/image.getHeight());
		}
		
		dest = new Rect(L.x-w, L.y-h, L.x+w, L.y+h);
		//TODO: rotate dest rect if needed
		//dest.set(left, top, right, bottom)
		
		c.drawBitmap(image, source, dest, null);	
	}
	
	public void nextFrame(){
		currentFrame++;
		this.load();
	}
}
