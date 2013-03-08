package edu.usf.eng.pie.avatars4change.wallpaper;

import android.graphics.Canvas;
import edu.usf.eng.pie.avatars4change.avatar.Avatar;
import edu.usf.eng.pie.avatars4change.avatar.Scene;

public class MainView {
	static Scene avatarScene;
	
	public static void setup(Avatar daAvatar){
		//setupAvatar Scene
		avatarScene = new Scene("avatarScene");
       	avatarScene.addEntity(daAvatar);
       	
       	DebugInfoView.setup();
	}
	public static void nextFrame(){
		avatarScene.nextFrame();
		DebugInfoView.nextFrame();
	}

	public static void draw(Canvas c){
		avatarScene.draw(c);
		DebugInfoView.drawFPS(c, avatarWallpaper.desiredFPS);
		UserStatusView.draw(c);
	}
}
