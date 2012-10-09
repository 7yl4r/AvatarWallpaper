package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.content.res.Resources;
import android.os.Environment;

public class sprite {
	//--- fields ---------------------------
	String baseFileDirectory = (Environment.getExternalStorageDirectory()).getAbsolutePath() + "/MirrorMe";		//file directory to use on sdcard
	String FileName = baseFileDirectory + "/sprites";
	int currentFrame = 0;
	int nFrames = 0;
	
	//center locations
	int x[];
	int y[];
	
	//sizes
	int sx[];
	int sy[];
	
	public sprite(String fName) {	//constructor
			//TODO set file name
			//TODO set nFrames
			//TODO set locations
		//		x = new int[nFrames];
	}
	
}
