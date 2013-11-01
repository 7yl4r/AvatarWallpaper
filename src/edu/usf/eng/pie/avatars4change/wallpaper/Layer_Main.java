package edu.usf.eng.pie.avatars4change.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import edu.usf.eng.pie.avatars4change.R;
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

	public static void draw(Context ctx, Canvas c, Rect frame, Avatar daAvatar){
		drawBG(ctx,c,frame,daAvatar);
		
		c.save();
		avatarScene.draw(c);	//this should already be scaled correctly
		c.restore();
		
		drawAbove(c,frame,daAvatar);
		
		//DEBUG text layers on very top:
		boolean debug = ctx.getSharedPreferences(ctx.getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
				.getBoolean(ctx.getString(R.string.key_debugmode),true);
		if(debug){
			c.save();
			Layer_DebugInfo.drawFPS(c, avatarWallpaper.desiredFPS, frame, daAvatar);
			c.restore();
		}
	}
	
	//settings-dependent background draw 
	private static void drawBG(Context ctx, Canvas c, Rect frame, Avatar daAvatar){
		c.save();
		if ( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("constant") ){
    		Layer_Background.drawPlainBG(c);
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("Proteus Effect Study")){
    		Layer_Background.drawPlainBG(c);
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("IEEE VR demo")){
    		boolean debug = ctx.getSharedPreferences(ctx.getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
    				.getBoolean(ctx.getString(R.string.key_debugmode),true);
    		String activMonitor = ctx.getSharedPreferences(ctx.getString(R.string.shared_prefs_name), Context.MODE_PRIVATE)
    				.getString(ctx.getString(R.string.key_activitymonitor),"");
    		if(debug && activMonitor.equals("built-in")){	//TODO: this should probably be referenced using R.string
    			Layer_Background.drawFFT_BG(c);
    		} else {
    			Layer_Background.drawPlainBG(c);
    		}
    	}else{
    		Log.e(TAG, "unrecognized scene behavior " + daAvatar.behaviorSelectorMethod);
    		Layer_Background.drawPlainBG(c);
    	}
		
	}
	
	//settings-dependent draw on top
	private static void drawAbove(Canvas c, Rect frame, Avatar daAvatar){
		if ( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("constant") ){
    		
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("Proteus Effect Study")){
		
    	}else if( daAvatar.behaviorSelectorMethod.equalsIgnoreCase("IEEE VR demo")){
    		c.save();
    		Layer_UserStatus.draw(c, frame);
    		c.restore();
    	}else{
    		Log.e(TAG, "unrecognized scene behavior " + daAvatar.behaviorSelectorMethod);
    	}
	}
}
