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
	
	Bitmap image; //current image
	
	sprite frame[];	//frames in the animation are sprites
	
	location L[];
	
	public animation(String nam, String fDir, location L) {
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
	
	
	public void draw(Canvas c){
		if(image == null)	return;	//don't draw if no image
		Rect source, dest;
		source = new Rect(0, 0, image.getWidth(), image.getHeight());
		//dest = new Rect((int)(L[currentFrame].x-L[currentFrame].sx/2),
		//				(int)(y[currentFrame]-sy[currentFrame]/2),
		//				(int)(x[currentFrame]+sx[currentFrame]/2),
		//				(int)(y[currentFrame]+sy[currentFrame]/2));
		//c.drawBitmap(image, source, dest, null);
	}
	
	public void nextFrame(){
		currentFrame++;
		this.load();
	}
}
