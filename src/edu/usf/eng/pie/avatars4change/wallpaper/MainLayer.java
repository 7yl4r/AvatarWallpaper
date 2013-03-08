package edu.usf.eng.pie.avatars4change.wallpaper;

import android.graphics.Canvas;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Scene;

public class MainLayer {
	static Scene avatarScene;
	
	public static void setup(Avatar daAvatar){
		//setupAvatar Scene
		avatarScene = new Scene("avatarScene");
       	avatarScene.addEntity(daAvatar);
       	
       	DebugInfoLayer.setup();
	}
	public static void nextFrame(){
		avatarScene.nextFrame();
		DebugInfoLayer.nextFrame();
	}

	public static void draw(Canvas c){
		c.save();
		avatarScene.draw(c);
		c.restore();
		c.save();
		DebugInfoLayer.drawFPS(c, avatarWallpaper.desiredFPS);
		c.restore();
		c.save();
		UserStatusLayer.draw(c);
		c.restore();
	}
}
