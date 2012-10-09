package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class sprite {
	//--- fields ---------------------------
	String fileDir = "notAFileName";
	int currentFrame = 0;
	int nFrames = 0;
	
	//center locations
	int x[];
	int y[];
	
	//sizes
	int sx[];
	int sy[];
	
	Bitmap image;
	
	public sprite(String fName) {	//constructor
			//TODO set file name
			//TODO set nFrames
			//TODO set locations
		//		x = new int[nFrames];
	}
	
	public void load(){
		image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
		if(image == null){
			currentFrame = 0;	//animation loops back
			//load in images from MirrorMe sdcard directory
			image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
			if(image == null){	//if still null
				Log.e("MirrorMe Avatar", "body sprite at " + fileDir+currentFrame+".png" +" not found!");	//something went wrong
			}
		}
	}
	
	public void draw(Canvas c, float surfaceX, float surfaceY){
		Rect source, dest;
		source = new Rect(0, 0, image.getWidth(), image.getHeight());
		dest = new Rect((int) (this.x[currentFrame]-sx[currentFrame]/2),	
						(int) (this.y[currentFrame]-sy[currentFrame]/2),
						(int) (this.x[currentFrame]+sx[currentFrame]/2),
						(int) (this.y[currentFrame]+sy[currentFrame]/2));
		c.drawBitmap(image, source, dest, null);	
	}
	}
	
}
