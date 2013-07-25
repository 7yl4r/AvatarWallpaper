package edu.usf.eng.pie.avatars4change.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Animation {
	private final String TAG = "animation";

	int MAXFRAMES = 15; // maximum number of frames in the sprite

	// animation frames
	int currentFrame = 0;
	public int nFrames = 0; // number of frames

	String fileDir = "notAFileName"; // init string (overwritten in constructor
	String name = "UNNAMED";

	// TODO this allocation for frames[] wastes memory, better to get
	// frameCount, then allocate
	// ... use something like:
	// List<Bitmap> frame = new ArrayList<Bitmap>();
	Bitmap frame[] = new Bitmap[MAXFRAMES]; // frames in the animation

	Location location;

	// constructor
	public Animation(String newName, String fDir, Location newLocation) {
		set(newName, fDir, newLocation);
	}

	public void set(String newName, String fDir, Location newLocation) {
		name = newName;
		if(fDir == null){
			Log.e(TAG,"given file directory for animation is null!");
		} else {
			fileDir = fDir; // set file directory
		}
		location = newLocation;
		this.load(); // frameCount is set by load()
		// allocate space for arrays???
		// set up spriteSheet???
	}

	// loads current animation image
	public void load() {
		int count = 0;
		do {
			String fName = fileDir + Integer.toString(count) + ".png";
			Log.v(TAG, "loading " + fName);
			frame[count] = BitmapFactory.decodeFile(fName);
			count++;
		} while (frame[count - 1] != null);
		Log.v(TAG, fileDir + Integer.toString(count) + ".png"
				+ " not found, stopping file setup");
		nFrames = count - 2;// at loop exit, count is 1 too large (acounting
							// from 0)
		Log.v(TAG, Integer.toString(count - 1)
				+ " frames loaded into animation " + name);
	}

	public void draw(Canvas c) {
		//Log.d(TAG, "F:" + Integer.toString(currentFrame) + " nF:" +
		// Integer.toString(nFrames));
		c.save();
		if (frame[currentFrame] == null) {
			Log.e(TAG,
					"cannot draw animation frame# "
							+ Integer.toString(currentFrame) + " in '" + fileDir
							+ "' for animation '"+this.name+"'; no image!");
			return; // don't draw if no image
		}
		// set up scaling of image
		Rect source = new Rect(0, 0, frame[currentFrame].getWidth(),
				frame[currentFrame].getHeight());
		int w = 0, h = 0; // image width & height (actually radius of image)
		if (frame[currentFrame].getWidth() > frame[currentFrame].getHeight()) {
			w = location.size;
			h = Math.round((float) location.size
					* ((float) frame[currentFrame].getHeight() / (float) frame[currentFrame]
							.getWidth()));
		} else {
			h = location.size;
			w = Math.round((float) location.size
					* ((float) frame[currentFrame].getWidth() / (float) frame[currentFrame]
							.getHeight()));
		}
		// Rect dest = new Rect(L.x-w/2, L.y-h/2, L.x+w/2, L.y+h/2);
		c.translate(location.x, -location.y);
		c.rotate(location.rotation);
		Rect dest = new Rect(-w / 2, -h / 2, w / 2, h / 2);
		c.drawBitmap(frame[currentFrame], source, dest, null);
		c.restore();
		; //Log.d(TAG, w + "x" + h + " animation @ frame " + currentFrame
			//	+ " drawn at " + location.x + "," + location.y);
	}

	public void nextFrame() {
		currentFrame++;
		if (currentFrame > nFrames) // reset if out of frames
			currentFrame = 0;
	}

	public void resetFrameCount() {
		currentFrame = 0;
	}

	public void setLocation(Location newLoc) {
		location = newLoc;
	}
}
