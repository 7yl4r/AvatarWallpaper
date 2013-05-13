package edu.usf.eng.pie.avatars4change.wallpaper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Scene;

public class Layer_Main {
	private static String TAG = "Layer_Main";
	static Scene avatarScene;
	
	public static void setup(Avatar daAvatar){
		Layer_Background.setup();
		
		//setupAvatar Scene
		avatarScene = new Scene("avatarScene");
       	avatarScene.addEntity(daAvatar);
       	
       	Layer_DebugInfo.setup();
	}
	public static void nextFrame(){
		Layer_Background.nextFrame();
		
		avatarScene.nextFrame();
		Layer_DebugInfo.nextFrame();
	}

	public static void draw(Canvas c, Rect frame, Avatar daAvatar){
		Layer_Background.draw(c);
		
		c.save();
		avatarScene.draw(c);	//this should already be scaled correctly
		c.restore();
		
		//settings-dependent draw:
		if ( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("constant") ){
    		
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("Proteus Effect Study")){
		
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("IEEE VR demo")){
    		c.save();
    		Layer_UserStatus.draw(c, frame);
    		c.restore();
    	}else{
    		Log.e(TAG, "unrecognized scene behavior " + daAvatar.behaviorSelectorMethod);
    	}
		
		//DEBUG layers:
		/*
		c.save();
		Layer_DebugInfo.drawFPS(c, avatarWallpaper.desiredFPS, frame);
		c.restore();
		*/
	}
}
