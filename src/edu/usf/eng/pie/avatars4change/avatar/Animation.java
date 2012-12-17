package edu.usf.eng.pie.avatars4change.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Animation {
	int MAXFRAMES = 15;		//maximum number of frames in the sprite
	
	//animation frames
	int currentFrame = 0;
	public int nFrames = 0;	//number of frames
	
	String fileDir = "notAFileName";	//init string (overwritten in constructor
	String name = "UNNAMED";
		
	//TODO this allocation for frames[] wastes memory, better to get frameCount, then allocate 
	// ... use something like:
	//	List<Bitmap> frame = new ArrayList<Bitmap>();
	Bitmap frame[] = new Bitmap[MAXFRAMES];	//frames in the animation

	
	Location L;
	
	//constructor
	public Animation(String newName, String fDir, Location newLocation) {
		set(newName,fDir,newLocation);
	}
	
	public void set(String newName, String fDir, Location newLocation){
		name = newName;
		fileDir = fDir;	//set file directory
		L = newLocation;
		this.load();	//frameCount is set by load()
		//allocate space for arrays???
		//set up spriteSheet???
	}
	
	//loads current animation image
	public void load(){
		int count = 0;
		do{
			String fName = fileDir+Integer.toString(count)+".png";
			Log.v("animation load","loading " + fName);
			frame[count] = BitmapFactory.decodeFile(fName);
			count++;
		}while(frame[count-1] != null);
		Log.v("animation load",fileDir+Integer.toString(count)+".png" + " not found, stopping file setup");
		nFrames = count-2;//at loop exit, count is 1 too large (counting from 0)
	}
	
	public void draw(Canvas c){
		//Log.v("animation", "F:" + Integer.toString(currentFrame) + " nF:" + Integer.toString(nFrames));
		c.save();
		c.translate(L.x, L.y);	//move to location of animation
		c.rotate(L.rotation);	

		if(frame[currentFrame] == null){
			Log.e("sprite","cannot draw sprite frame "+Integer.toString(currentFrame)+" in "+fileDir+", no image!");
			return;	//don't draw if no image
		}
		//set up scaling of image
		Rect source = new Rect(0, 0, frame[currentFrame].getWidth(), frame[currentFrame].getHeight());		
		int w = 0,h = 0;	//image width & height (actually radius of image)
		if(frame[currentFrame].getWidth()>frame[currentFrame].getHeight()){ 
			w = L.size;
			h = Math.round( (float)L.size * ((float)frame[currentFrame].getHeight()/(float)frame[currentFrame].getWidth()) );
		} else {
			h = L.size;
			w = Math.round( (float)L.size * ((float)frame[currentFrame].getWidth()/(float)frame[currentFrame].getHeight()) );
		}
		Rect dest = new Rect(L.x-w/2, L.y-h/2, L.x+w/2, L.y+h/2);
		c.drawBitmap(frame[currentFrame], source, dest, null);
		c.restore();
	}
	
	public void nextFrame(){
		currentFrame++;
		if( currentFrame > nFrames )	//reset if out of frames
			currentFrame = 0;
	}
}
