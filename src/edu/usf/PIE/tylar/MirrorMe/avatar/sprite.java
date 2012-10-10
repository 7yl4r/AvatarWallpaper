package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class sprite {
	int MAXFRAMES = 15;
	
	//--- fields ---------------------------
	String fileDir = "notAFileName";
	int currentFrame = 0;
	public int nFrames = 0;
	
	//center locations
	int x[] = new int[MAXFRAMES];
	int y[] = new int[MAXFRAMES];
	
	//sizes
	int sx[] = new int[MAXFRAMES];
	int sy[] = new int[MAXFRAMES];
	
	Bitmap image;

	public sprite(String fDir) {	//constructor
			fileDir = fDir;	//set file directory
			//TODO set number of frames
			//allocate space for arrays
			this.load();
	}
	
	public void load(){	//loads next image
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
	
	public void nextFrame(){
		currentFrame++;
		this.load();
	}
}
