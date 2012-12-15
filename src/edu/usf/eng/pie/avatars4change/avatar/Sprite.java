package edu.usf.eng.pie.avatars4change.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {
	
	String name = "UNNAMED";
	Location L = new Location();
	
	Bitmap image;
	
	//constructor
	public Sprite(String newName, String fileName, Location newLocation){
		name = newName;
		loadImage(fileName);
		L    = newLocation;
	}

	public void loadImage(String fName){
		//image = BitmapFactory.decodeFile(fileDir+currentFrame+".png");
		image = BitmapFactory.decodeFile(fName);
		if(image==null) Log.e("sprite","file " + fName + " failed to load!");
	}
	
	//draws the sprite on given canvas c at object location relative to given location L
	public void draw(Canvas c){
		if(image == null){
			Log.e("sprite","cannot draw sprite, no image!");
			return;	//don't draw if no image
		}
		Rect source, dest;
		source = new Rect(0, 0, image.getWidth(), image.getHeight());//TODO to use sprite sheet, adjust this to select part of image
		int w = 0,h = 0;	//image width & height (actually radius of image)
		if(image.getWidth()>image.getHeight()){ 
			w = L.size;
			h = Math.round( (float)L.size * ((float)image.getHeight()/(float)image.getWidth()) );
		} else {
			h = L.size;
			w = Math.round( (float)L.size * ((float)image.getWidth()/(float)image.getHeight()) );
		}
		dest = new Rect(L.x-w/2, L.y-h/2, L.x+w/2, L.y+h/2);
		//Log.v("sprite","w=" + Integer.toString(w) + " h=" + Integer.toString(h));
		c.rotate(L.rotation);
		c.drawBitmap(image, source, dest, null);
		c.rotate(-L.rotation);
	}
}
